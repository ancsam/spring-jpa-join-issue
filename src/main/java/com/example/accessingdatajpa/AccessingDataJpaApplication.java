package com.example.accessingdatajpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class AccessingDataJpaApplication {

	@PersistenceContext
	private EntityManager entityManager;

	private static final Logger log = LoggerFactory.getLogger(AccessingDataJpaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AccessingDataJpaApplication.class);
	}

	@Bean
	public CommandLineRunner demo(GEntityRepository gRepository, PEntityRepository pRepository, GPEntityRepository gpRepository) {
		return (args) -> {

			GEntity gEntity = new GEntity("g1");
			gRepository.save(gEntity);

			PEntity[] pEntities = new PEntity[3];
			GPEntity[] gpEntities = new GPEntity[3];

			for (int i = 0; i < 3; i++) {
				pEntities[i] = new PEntity("p" + i, false);
				pRepository.save(pEntities[i]);

				gpEntities[i] = new GPEntity(gEntity, pEntities[i]);
				gpRepository.save(gpEntities[i]);
				gEntity.getGpEntitySet().add(gpEntities[i]);
			}
			gRepository.save(gEntity);

			// query with issues
			CriteriaBuilder builder = entityManager.getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery<Tuple> criteriaQuery = builder.createQuery(Tuple.class);
			Root<GPEntity> root = criteriaQuery.from(GPEntity.class);
			Join<GPEntity, PEntity> pEntityJoin = root.join("pEntity", JoinType.LEFT);
			pEntityJoin.on(builder.equal(pEntityJoin.get("active"), true));

			criteriaQuery.select(builder.tuple(root.get("gEntity").get("id"),
							builder.countDistinct(root.get("id")),
							builder.countDistinct(pEntityJoin.get("id"))))
					.distinct(true);

			criteriaQuery.groupBy(root.get("gEntity").get("id"));

			entityManager.createQuery(criteriaQuery).getResultList().forEach(item -> {
				log.info(">> root.id: " + item.get(0, Long.class) + " - countDistinct root.id: "
						+ item.get(1, Long.class) + " - countDistinct pEntity.id " + item.get(2, Long.class));
			});

			// spring boot 3.1.0
			// Hibernate: select distinct g1_0.g_entity_id c0,count(distinct g1_0.id) c1,count(distinct p1_0.id) c2 from gpentity g1_0 left join pentity p1_0 on p1_0.id=g1_0.p_entity_id and p1_0.active=? group by c0
			//root.id: 1 - countDistinct root.id: 3 - countDistinct pEntity.id 0

			// spring boot 3.1.1 -> 3.1.4
			// Hibernate: select distinct g1_0.g_entity_id c0,count(distinct g1_0.id) c1,count(distinctd g1_0.p_entity_i) c2 from gpentity g1_0 left join pentity p1_0 on p1_0.id=g1_0.p_entity_id and p1_0.active=? group by c0
			// root.id: 1 - countDistinct root.id: 3 - countDistinct pEntity.id 3

			// see difference in 3rd select element: p1_0.id vs g1_0.p_entity_id --> since we have a LEFT JOIN p1_0 will be NULL, but g1_0 will have a value

		};
	}

}
