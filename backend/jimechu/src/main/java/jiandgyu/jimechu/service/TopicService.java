package jiandgyu.jimechu.service;

import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.domain.Menu;
import jiandgyu.jimechu.domain.Topic;
import jiandgyu.jimechu.domain.Visibility;
import jiandgyu.jimechu.repository.MemberRepository;
import jiandgyu.jimechu.repository.MenuRepository;
import jiandgyu.jimechu.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;

    /**
     * Topic 생성
     */
    @Transactional
    public Long createTopic(Long memberId, String title, Visibility visibility, ArrayList<String> menus) {

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 memeberId : " + memberId);
        }

        // Topic 생성
        Topic topic = Topic.createTopic(title, member, visibility);

        // Menu 생성
        if (menus != null) {
            for (String menuName : menus) {
                Menu menu = Menu.createMenu(menuName, topic);
                menuRepository.save(menu);
            }
        }

        // Topic 저장
        topicRepository.save(topic);
        return topic.getId();
    }

    /**
     * 전체 Topic 조회
     */
    public List<Topic> findTopics(Boolean isAuthenticated, Long memberId, boolean isAdmin) {
        if (isAdmin) {
            // 관리자(ADMIN)는 모든 PUBLIC + PRIVATE 토픽 조회
            return topicRepository.findAll();
        } else if (isAuthenticated) {
            // 로그인한 사용자는 자신의 PRIVATE + 모든 PUBLIC 토픽 조회
            return topicRepository.findPublicAndPrivateTopicsByMemberId(memberId);
        } else {
            // 비로그인 사용자는 PUBLIC 토픽만 조회 가능
            return topicRepository.findPublicTopics();
        }
    }

    /**
     * 특정 Member에 속한 Topic 조회
     */
    public List<Topic> findTopicsByMemberId(Long memberId) {
        return topicRepository.findAllByMemberId(memberId);
    }

    /**
     * topicId로 topic 조회
     */
    public Topic getTopicById(Long topicId) {
        return topicRepository.findOne(topicId);
    }

    /**
     * topic update (title 바꾸기)
     */
    @Transactional
    public void updateTopicTitle(Long topicId, String title) {
        topicRepository.update(topicId, title);
    }

    /**
     * topicId로 topic title 조회
     */
    public String getTopicTitleById(Long topicId) {
        return topicRepository.findOne(topicId).getTitle();
    }

    /**
     * topicId로 topic visibility 조회
     */
    public String getTopicVisibilityById(Long topicId) {
        return topicRepository.findOne(topicId).getVisibility().name();
    }

    /**
     * topic 삭제, topic에 속한 menu도 삭제
     */
    @Transactional
    public void deleteTopicAndMenus(Long topicId) {
        menuRepository.deleteByTopicId(topicId);
        topicRepository.delete(topicId);
    }
}
