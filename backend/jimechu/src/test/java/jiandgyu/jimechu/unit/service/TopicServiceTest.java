package jiandgyu.jimechu.unit.service;

import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.domain.Menu;
import jiandgyu.jimechu.domain.Topic;
import jiandgyu.jimechu.repository.MemberRepository;
import jiandgyu.jimechu.repository.MenuRepository;
import jiandgyu.jimechu.repository.TopicRepository;
import jiandgyu.jimechu.service.MemberService;
import jiandgyu.jimechu.service.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TopicServiceTest {

    @Mock private MemberRepository memberRepository;
    @Mock private MenuRepository menuRepository;
    @Mock private TopicRepository topicRepository;
    @InjectMocks private TopicService topicService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void 토픽_생성() {
        // given
        Member member = new Member();
        member.setId(1L);
        member.setNickname("지나");
        member.setPassword("지나123");

        when(memberRepository.findOne(1L)).thenReturn(member);

        Topic topic = new Topic();
        topic.setId(1L);
        topic.setMember(member);
        topic.setTitle("지메추");

        // Mock : save 호출 시 TopicRepository Mocking
        when(topicRepository.save(any(Topic.class))).thenAnswer(invocation -> {
            Topic savedTopic = invocation.getArgument(0);
            savedTopic.setId(1L);
            return savedTopic;
        });

        // when
        Long topicId = topicService.createTopic(1L, "지메추", List.of());

        // then
        assertEquals(topicId, 1L);
        assertEquals(topic.getTitle(), "지메추");

        assertEquals(topic.getMember().getId(), 1L);
        assertEquals(topic.getMember().getNickname(), "지나");
        assertEquals(topic.getMember().getPassword(), "지나123");
    }

    @Test
    public void 토픽_삭제_하위메뉴_모두_삭제() {
        // given
        Topic topic = new Topic();
        topic.setId(1L);
        topic.setTitle("지메추");

        Menu menu1 = new Menu();
        menu1.setId(1L);
        menu1.setName("메뉴1");
        menu1.setTopic(topic);

        Menu menu2 = new Menu();
        menu2.setId(2L);
        menu2.setName("메뉴2");
        menu2.setTopic(topic);

        topic.setMenus(List.of(menu1, menu2));

        // Mock : findOne 호출 시 TopicRepository Mocking
        when(topicRepository.findOne(1L)).thenReturn(topic);

        // Mock : TopicRepository, MenuRepository Mocking
        doNothing().when(menuRepository).deleteByTopicId(1L);
        doNothing().when(topicRepository).delete(1L);


        // when
        topicService.deleteTopicAndMenus(1L);

        // then
        verify(menuRepository, times(1)).deleteByTopicId(1L);
        verify(topicRepository, times(1)).delete(1L);
    }

}