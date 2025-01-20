package jiandgyu.jimechu.service;

import jiandgyu.jimechu.config.security.CustomMember;
import jiandgyu.jimechu.domain.Member;
import jiandgyu.jimechu.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1) Fetch from DB
        List<Member> members = memberRepository.findByNickname(username);
        log.debug("loadUserByUsername called with username: {}", username);

        // 2) Throw if none found
        if (members.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }

        // 3) Take the first matching record
        Member member = members.get(0);
        log.debug("Found member: {}", member);
        log.debug("DB에서 불러온 member.password: {}", member.getPassword());

        // 4) Convert to UserDetails
        return createUserDetails(member);
    }

    private UserDetails createUserDetails(Member member) {
        log.debug("Creating UserDetails for member: {}", member);
        List<SimpleGrantedAuthority> authorities = member.getMemberRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole()))
                .toList();
        log.debug("authorities : {}", authorities);

        return new CustomMember(member.getId(), member.getNickname(), member.getPassword(), authorities);
    }
}
