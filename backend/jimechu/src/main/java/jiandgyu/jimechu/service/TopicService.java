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

    // 생성
    @Transactional
    public Long topic(Long memberId) {

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);

        // menu 생성...

        // Topic 생성
        Topic topic = Topic.createTopic(member);

        // Topic 저장
        topicRepository.save(topic);
        return topic.getId();
    }

    public List<Topic> findTopics() {
        return topicRepository.findAll();
    }

    // 취소

    // 검색
}
