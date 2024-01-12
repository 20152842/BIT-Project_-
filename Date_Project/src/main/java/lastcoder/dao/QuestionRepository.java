package lastcoder.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import lastcoder.dto.Question;

//엔티티만으로는 데이터베이스에 데이터를 저장하거나 조회 할 수 없다. 
//데이터 처리를 위해서는 실제 데이터베이스와 연동하는 JPA 리포지터리가 필요하다.


public interface QuestionRepository extends JpaRepository<Question, Integer>  {
	Question findBySubject(String subject);
	Question findBySubjectAndContent(String subject, String content);
	List<Question> findBySubjectLike(String subject);
	
	Page<Question> findAll(Pageable pageable);
//	Page<Question> findAll(Specification<Question> spec, Pageable pageable);
	
	@Query("select "
            + "distinct q "
            + "from Question q " 
            + "left outer join SiteUser u1 on q.author=u1 "
            + "left outer join Answer a on a.question=q "
            + "left outer join SiteUser u2 on a.author=u2 "
            + "where "
            + "   q.subject like %:kw% "
            + "   or q.content like %:kw% "
            + "   or u1.username like %:kw% "
            + "   or a.content like %:kw% "
            + "   or u2.username like %:kw% ")
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

}
