package pl.vavatech.auction.blc;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Ignore
@Transactional(readOnly = false)
@Rollback
@ContextConfiguration(classes = BusinessConfig.class)
public class AbstractIntegrationTest {
	@PersistenceContext
	private EntityManager em;

	protected Long count(Class entity) {
		CriteriaBuilder qb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = qb.createQuery(Long.class);
		cq.select(qb.count(cq.from(entity)));
		return em.createQuery(cq).getSingleResult();
	}

}
