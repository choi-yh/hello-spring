package hello.hello_spring.service;

import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MemberServiceTest {

    MemoryMemberRepository memberRepository;
    MemberService memberService;

    @BeforeEach
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void join() {
        // given
        Member member = new Member();
        member.setName("name");

        // when
        Long id = memberService.join(member);

        // then
        Member findMember = memberService.findOne(id).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    void duplicateException() {
        // given
        Member member1 = new Member();
        member1.setName("name1");

        Member member2 = new Member();
        member2.setName("name1");

        // when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertThat(e.getMessage()).isEqualTo("존재하는 회원입니다.");

        // then
    }

    @Test
    void findMembers() {
        Member member1 = new Member();
        Member member2 = new Member();
        Member member3 = new Member();
        member1.setName("member1");
        member2.setName("member2");
        member3.setName("member3");

        memberService.join(member1);
        memberService.join(member2);
        memberService.join(member3);

        List<Member> members = memberService.findMembers();
        assertThat(members.size()).isEqualTo(3);
    }

    @Test
    void findOne() {
        Member member1 = new Member();
        Member member2 = new Member();
        Member member3 = new Member();
        member1.setName("member1");
        member2.setName("member2");
        member3.setName("member3");

        memberService.join(member1);
        memberService.join(member2);
        memberService.join(member3);

        Member result = memberService.findOne(member1.getId()).get();
        assertThat(result).isEqualTo(member1);
    }
}
