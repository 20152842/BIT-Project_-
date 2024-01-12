package lastcoder.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import lastcoder.dto.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

}
