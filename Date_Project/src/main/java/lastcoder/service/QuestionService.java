package lastcoder.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lastcoder.config.DataNotFoundException;
import lastcoder.dao.QuestionRepository;
import lastcoder.dto.Answer;
import lastcoder.dto.Question;
import lastcoder.dto.SiteUser;
//스프링의 서비스로 만들기 위해서는 위와 같이 클래스명 위에 
//@Service 애너테이션을 붙이면 된다. @Controller, @Entity 등과 마찬가지로 
//스프링부트는 @Service 애너테이션이 붙은 클래스는 서비스로 인식한다.
//questionRepository 객체는 생성자 방식으로 DI 규칙에 의해 주입된다.
//그리고 질문 목록을 조회하여 리턴하는 getList 메서드를 추가했다. 
//이전 컨트롤러에서 리포지터리를 사용했던 부분을 그대로 옮긴 것이다.

//브라우저로 http://localhost:8080/question/list 페이지에 접속하면 이전과 동일한 화면을 볼수 있다. 
//앞으로 작성할 컨트롤러들도 리포지터리를 직접 사용하지 않고 
//Controller -> Service -> Repository 구조로 데이터를 처리할 것이다.

@Service
public class QuestionService {

	private QuestionRepository questionRepository;

	private Specification<Question> search(String kw) {
		return new Specification<>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true); // 중복을 제거
				Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
				Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
				Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
				return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
						cb.like(q.get("content"), "%" + kw + "%"), // 내용
						cb.like(u1.get("username"), "%" + kw + "%"), // 질문 작성자
						cb.like(a.get("content"), "%" + kw + "%"), // 답변 내용
						cb.like(u2.get("username"), "%" + kw + "%")); // 답변 작성자
			}
		};
	}

	public Page<Question> getList() {

		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "username"));
		Page<Question> page = questionRepository.findAll(pageRequest);

		page.map(Question -> new Question());

		List<Question> content = page.getContent();

		long totalElements = page.getTotalElements();
		
//		getNumber를 통해 현재 페이지를 알 수 있다.
//		getSize를 통해 현재 페이지의 Limit를 알 수 있다. 
//		getNumberOfElements를 통해 현재 페이지에 나올 데이터 수를 알 수 있다.
//		isFirst를 통해 첫번째 페이지인지 확인할 수 있다. JPA는 0번부터 시작이기 때문에 False가 나왔다.
//		다음 페이지가 있는지, 이전 페이지가 있는지도 확인할 수 있다. 
		

        // 멤버 객체의 사이즈 확인)
        System.out.println(content.size());
        
        // 토탈 페이지수 확인
        System.out.println(page.getTotalElements());
        
        // 페이지 번호 가져오기. JPA 페이지 시작 번호는 0
        System.out.println(page.getNumber());

        // 전체 토탈 페이지 개수 확인 (3명 / 2명, limit3이기 때문에 페이지는 2개)
        System.out.println(page.getTotalPages());

        // 첫번째 페이지인지 알려준다.
        System.out.println(page.isFirst());

        // 다음 페이지가 있는지도 알려준다.
        System.out.println(page.hasNext());
		
		for (Question question : content) {
			System.out.println("question = " + question);
		}

		System.out.println("totalElements = " + totalElements);
		return page;
	}

	public Question getQuestion(Integer id) {
		Optional<Question> question = questionRepository.findById(id);
		if (question.isPresent()) {
			return question.get();
		} else {
			throw new DataNotFoundException("question not found");
		}
	}

	public Page<Question> getList(int page, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Specification<Question> spec = search(kw);
		return questionRepository.findAllByKeyword(kw, pageable);
	}

	public void create(String subject, String content, SiteUser user) {
		Question q = new Question();
		q.setSubject(subject);
		q.setContent(content);
		q.setCreateDate(LocalDateTime.now());
		q.setAuthor(user);
		questionRepository.save(q);
	}

	public void modify(Question question, String subject, String content) {
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(LocalDateTime.now());
		questionRepository.save(question);
	}

	public void delete(Question question) {
		questionRepository.delete(question);
	}

	public void vote(Question question, SiteUser siteUser) {
		question.getVoter().add(siteUser);
		questionRepository.save(question);
	}
}
