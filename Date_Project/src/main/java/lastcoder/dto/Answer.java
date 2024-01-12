package lastcoder.dto;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Answer {
	
	private LocalDateTime modifyDate;
	
	
    @Id //고유 번호 id 속성에 적용한 @Id 애너테이션은 id 속성을 기본 키로 지정한다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//답변의 고유 번호
//    @GeneratedValue 애너테이션을 적용하면 데이터를 저장할 때 해당 속성에 값을 따로 세팅하지 않아도 
//    1씩 자동으로 증가하여 저장된다
//    strategy는 고유번호를 생성하는 옵션으로 GenerationType.IDENTITY는 해당 컬럼만의 독립적인 시퀀스를 생성하여 
//    번호를 증가시킬 때 사용한다.
//    strategy 옵션을 생략할 경우에 @GeneratedValue 애너테이션이 지정된 컬럼들이 모두 동일한 
//    시퀀스로 번호를 생성하기 때문에 일정한 순서의 고유번호를 가질수 없게 된다. 
//    이러한 이유로 보통 GenerationType.IDENTITY를 많이 사용한다.


    
    @Column(columnDefinition = "TEXT")
    private String content;//답변의 내용
//    엔티티의 속성은 테이블의 컬럼명과 일치하는데 컬럼의 세부 설정을 위해 @Column 애너테이션을 사용한다. 
//    length는 컬럼의 길이를 설정할때 사용하고 columnDefinition은 컬럼의 속성을 정의할 때 사용한다. 
//    columnDefinition = "TEXT"은 "내용"처럼 글자 수를 제한할 수 없는 경우에 사용한다.
//	엔티티의 속성은 @Column 애너테이션을 사용하지 않더라도 테이블 컬럼으로 인식한다. 
//	테이블 컬럼으로 인식하고 싶지 않은 경우에만 @Transient 애너테이션을 사용한다.
    
    
   
    @CreatedDate
    private LocalDateTime createDate; //답변을 작성한 일시
//    위의 Question 엔티티에서 작성일시에 해당하는 createDate 속성의 실제 테이블의 컬럼명은 create_date가 된다. 
//    즉 createDate처럼 대소문자 형태의 카멜케이스(Camel Case) 이름은 
//    create_date 처럼 모두 소문자로 변경되고 언더바(_)로 단어가 구분되어 실제 테이블 컬럼명이 된다.
 
    @ManyToOne
    private Question question; //질문 (어떤 질문의 답변인지 알아야하므로 질문 속성이 필요하다)

//      answer.getQuestion().getSubject()처럼 접근할 수 있다. 
//      하지만 이렇게 속성만 추가하면 안되고 질문 엔티티와 연결된 속성이라는 것을 명시적으로 표시해야 한다.
//      즉, 다음과 같이 question 속성에 @ManyToOne 애너테이션을 추가해야 한다.
//      @ManyToOne은 N:1 관계라고 할 수 있다. 
//      이렇게 @ManyToOne 애너테이션을 설정하면 Answer 엔티티의 question 속성과 Question 엔티티가 서로 연결된다. 
//      (실제 데이터베이스에서는 ForeignKey 관계가 생성된다.)
//      @ManyToOne은 부모 자식 관계를 갖는 구조에서 사용한다. 
//      여기서 부모는 Question, 자식은 Answer라고 할 수 있다.

    
    
    @ManyToOne
    private SiteUser author;
    
    @ManyToMany
    Set<SiteUser> voter;
}
