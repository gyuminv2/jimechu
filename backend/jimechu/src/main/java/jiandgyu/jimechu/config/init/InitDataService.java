package jiandgyu.jimechu.config.init;

import jakarta.annotation.PostConstruct;
import jiandgyu.jimechu.domain.*;
import jiandgyu.jimechu.repository.MemberRepository;
import jiandgyu.jimechu.repository.MemberRoleRepository;
import jiandgyu.jimechu.repository.MenuRepository;
import jiandgyu.jimechu.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitDataService {

    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final TopicRepository topicRepository;
    private final MenuRepository menuRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private static final List<String> MEAT = List.of(
            "삼겹살", "야키토리", "돼지곱창", "소곱창", "닭발", "후라이드치킨", "뒷고기", "회", "막창",
            "소고기", "돼지고기", "육회", "막창", "대창", "순대", "곱창전골", "순대전골", "생선구이",
            "생선조림", "게장", "돼지갈비", "소갈비", "규카츠", "감자탕", "뼈해장국", "족발", "수육",
            "보쌈", "탕수육", "냉삼", "차돌박이", "스테이크", "양념치킨", "순살치킨", "제육볶음", "연어",
            "쭈꾸미", "낙지볶음", "육사시미", "회덮밥", "카이센동", "모츠나베", "닭볶음탕", "비빔면",
            "대패삼겹살"
    );

    private static final List<String> DEFAULT_MENUS = Stream
            .concat(
                    Stream.of("한식", "양식", "중식", "아시안", "일식", "인도커리", "파스타", "피자", "국밥", "해장국",
                            "초밥", "라멘", "칼국수", "떡볶이", "순대국", "쌀국수", "순두부찌개", "김치찜", "김치찌개", "짬뽕",
                            "탕수육", "중식", "딤섬", "만두", "된장찌개", "새우볶음밥", "김밥", "돌솥비빔밥", "솥밥", "메밀소바",
                            "마제소바", "텐동", "냉면", "우동", "떡볶이", "마라탕", "마라샹궈", "알리오올리오", "아라비아따",
                            "까르보나라", "라구", "로제파스타", "봉골레파스타", "명란크림파스타", "감바스", "마르게리따", "고르곤졸라",
                            "냉우동", "냉모밀", "로스카츠", "히레카츠", "경양식돈까스", "우육면", "물냉면", "비빔냉면", "바지락칼국수",
                            "닭칼국수", "닭한마리", "즉석떡볶이", "리조또", "라면", "꿔바로우", "막국수", "수제비", "오므라이스", "카레",
                            "오징어덮밥", "오징어볶음", "갈비탕", "갈비찜", "뚝배기불고기", "부대찌개", "육개장", "새우볶음밥"
                    ),
                    MEAT.stream()
            )
            .distinct()
            .collect(Collectors.toList());

    private static final List<String> DESSERTS = List.of(
            "퀸아망", "딸기케이크", "초코케이크", "크로와상", "크로플", "크루키", "메론빵", "아이스크림", "몽블랑",
            "홍콩와플", "도넛", "빵", "와플", "딸기빙수", "초코빙수", "치즈빙수", "망고빙수", "인절미빙수",
            "빨미까레", "핫도그", "타코야끼", "감자튀김", "미트파이", "스콘"
    );

    private static final List<String> STONE_I_MENUS = List.of(
            "찜닭", "굽네", "피자", "순대국", "칼국수", "서브웨이", "버거킹", "보노보스", "야래향"
    );

    @Transactional
    public void initData() {
        log.info("=== Init 데이터 로딩 시작 ===");

        Member systemMember = new Member();
        systemMember.setNickname("jinkim2");
        systemMember.setPassword(passwordEncoder.encode("jinkim2")); // 임시 비밀번호
        memberRepository.save(systemMember);

        MemberRole memberRole = MemberRole.builder()
                .role(Role.ADMIN)
                .member(systemMember)
                .build();
        memberRoleRepository.save(memberRole);

        saveMenusAsTopic("지메추", DEFAULT_MENUS, systemMember);
        saveMenusAsTopic("지디추", DESSERTS, systemMember);
        saveMenusAsTopic("스메추", STONE_I_MENUS, systemMember);
        // 메메추는 뺐음

        log.info("=== 초기 데이터 로딩 완료 ===");
    }

    /**
     * 하나의 Topic 아래 Menu들로 저장
     */
    private void saveMenusAsTopic(String topicName, List<String> menuNames, Member owner) {
        // Topic 생성
        Topic topic = Topic.createTopic(topicName, owner, true);
        topicRepository.save(topic);

        for (String name : menuNames) {
            Menu menu = Menu.createMenu(name, topic);
            menuRepository.save(menu);
        }
        log.debug("Topic({})에 Menu {}개 저장 완료", topicName, menuNames.size());
    }
}