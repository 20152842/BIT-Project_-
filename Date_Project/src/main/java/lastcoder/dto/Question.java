package lastcoder.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Question {
	
	private LocalDateTime modifyDate;
	//질문의 고유 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //질문의 제목
    @Column(length = 200)
    private String subject;

    //질문의 내용
    @Column(columnDefinition = "TEXT")
    private String content;

    //질문을 작성한 일시
    private LocalDateTime createDate;
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
//  답변과 질문이 N:1의 관계라면 질문과 답변은 1:N의 관계라고 할 수 있다. 
//  이런경우에는 @ManyToOne이 아닌 @OneToMany애너테이션을 사용한다. 
//  Question 하나에 Answer는 여러개이므로 Question 엔티티에 추가할 답변의 속성은 List 형태로 구성해야 한다.
//  (예:question)에서 답변을 참조하려면 question.getAnswerList()를 호출하면 된다. 
//  @OneToMany 애너테이션에 사용된 mappedBy는 참조 엔티티의 속성명을 의미한다. 
//  즉, Answer 엔티티에서 Question 엔티티를 참조한 속성명 question을 mappedBy에 전달해야 한다.
    
//  질문 하나에는 여러개의 답변이 작성될 수 있다. 이때 질문을 삭제하면 그에 달린 답변들도 모두 함께 삭제하기 위해서 
//  @OneToMany의 속성으로 cascade = CascadeType.REMOVE를 사용했다.
    
    @ManyToOne
    private SiteUser author;
    
    @ManyToMany
    Set<SiteUser> voter;
}
