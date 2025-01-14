//package jiandgyu.jimechu.tmp;
//
//import jakarta.persistence.*;
//import jiandgyu.jimechu.domain.Menu;
//import lombok.Getter;
//import lombok.Setter;
//
//@Entity
//@Getter @Setter
//public class MenuCategory {
//
//    @Id @GeneratedValue
//    @Column(name = "menu_category_id")
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "menu_id")
//    private Menu menu;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "category_id")
//    private Category category;
//}
