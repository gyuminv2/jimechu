package jiandgyu.jimechu.service;

import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.domain.Menu;
import jiandgyu.jimechu.domain.Topic;
import jiandgyu.jimechu.repository.MemberRepository;
import jiandgyu.jimechu.repository.MenuRepository;
import jiandgyu.jimechu.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Long createTopic(Long memberId, String title, Boolean isPublic, List<Menu> menus) {

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 memeberId : " + memberId);
        }

        // Topic 생성
        Topic topic = Topic.createTopic(title, member, isPublic);

        // Topic 저장
        topicRepository.save(topic);
        return topic.getId();
    }

    /**
     * 전체 Topic 조회
     */
    public List<Topic> findTopics() {
        return topicRepository.findAll();
    }

    /**
     * 특정 Member에 속한 Topic 조회
     */
    public List<Topic> findTopicsByMemberId(Long memberId) {
        return topicRepository.findAllByMemberId(memberId);
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
     * topic 삭제, topic에 속한 menu도 삭제
     */
    @Transactional
    public void deleteTopicAndMenus(Long topicId) {
        menuRepository.deleteByTopicId(topicId);
        topicRepository.delete(topicId);
    }
}
