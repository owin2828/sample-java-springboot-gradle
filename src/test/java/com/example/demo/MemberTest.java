package com.example.demo;

import java.util.Optional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import junit.framework.Assert;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MemberTest extends MemberHibernateApplicationTests{
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Test
	public void a_join() {
		Member member =new Member();
		member.setAddress("ZZZZZZZZZZZZZZZZZZZZZZZ");
		member.setAge("ZZZ");
		member.setContent("ZZZZZZZZZZZZZZZZZZZZZZZ");
		member.setEmail("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		member.setPosition("ZZZZZZZZZ");
		member.setUser_id("ZZZZZZZZZZZZ");
		member.setUser_name("ZZZZZZZZZZZZZZZ");
		
		Member newMember = memberRepository.save(member);
		
		System.out.println(newMember);
	}
	
	
	
	  @Test public void b_update() {
	  Optional<Member> member=memberRepository.findById(Long.valueOf(1));
	  member.ifPresent(
			  selectMember->{
				  selectMember.setAddress("AAA");
				  selectMember.setAge("55");
				  selectMember.setContent("ZZZ");
				  selectMember.setEmail("AA@KT");
				  selectMember.setPosition("5G");
				  
				  Member newMember = memberRepository.save(selectMember);
				  System.out.println("member: " +newMember);
	  });
	  }
	  
	  @SuppressWarnings("deprecation")
	@Test public void c_delete() {
		  Optional<Member> member=memberRepository.findById(Long.valueOf(1));
		  
		  Assert.assertTrue(member.isPresent());
		  member.ifPresent(selectMember ->{
			  memberRepository.delete(selectMember);
		  });
		  
		  Optional<Member> deleteMember =memberRepository.findById(Long.valueOf(100));
		  Assert.assertFalse(deleteMember.isPresent());
		  }
}
