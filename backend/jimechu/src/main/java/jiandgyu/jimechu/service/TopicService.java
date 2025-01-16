package jiandgyu.jimechu.service;

import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.domain.Menu;
import jiandgyu.jimechu.domain.Topic;
import jiandgyu.jimechu.repository.MemberRepository;
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

    /**
     * Topic 생성
     */
    @Transactional
    public Long createTopic(Long memberId, String title, List<Menu> menus) {

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 memeberId : " + memberId);
        }

        // Topic 생성
        Topic topic = Topic.createTopic(title, member);

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
     * topic update도 있어야할듯 (title 바꾸기, menu 추가, menu 삭제)
     */


    /**
     * topic 삭제 ... 구현하려면 topic이 삭제되면 얘와 연관된 menu들도 전부 삭제되어야함...
     */
    public void deleteTopic(Long topicId) {
        topicRepository.delete(topicId);
    }
}
