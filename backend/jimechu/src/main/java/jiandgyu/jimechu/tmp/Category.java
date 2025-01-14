//package jiandgyu.jimechu.tmp;
//
//import jakarta.persistence.*;
//import jiandgyu.jimechu.domain.Member;
//import jiandgyu.jimechu.tmp.MenuCategory;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Getter @Setter
//public class Category {
//
//    @Id @GeneratedValue
//    @Column(name = "catergory_id")
//    private Long id;
//
//    private String name;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;
//
//    @OneToMany(mappedBy = "category")
//    private List<MenuCategory> menuCategories = new ArrayList<>();
//
//    public void setMember(Member member) {
//        this.member = member;
//        member.getCategories().add(this);
//    }
//
//}
