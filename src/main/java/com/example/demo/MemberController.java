package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {
	@Autowired
	MemberRepository repository;

	@GetMapping("/")
	public String home(Model model) {
		System.out.println("Start for master branch");
		List<Member> members = repository.findAll();

		model.addAttribute("members", members);
		return "index";
	}

	@GetMapping("/delete/{no}")
	public String delete(@PathVariable long no) {
		System.out.println(no);
		repository.deleteById(no);
		// System.out.println(result);
		return "redirect:/";
	}

	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("btn_name", "Register");
		return "view/register";
	}

	// 완료
	@PostMapping("/join")
	public String join(Member member, Model model) {
		repository.save(member);
		return "redirect:/";
	}

	@GetMapping("/edit/{no}")
	public String edit(@PathVariable long no, Model model) {
		System.out.println(no);
		Optional<Member> tmp = repository.findById(no);
		Member member = tmp.get();
		model.addAttribute("member", member);
		return "view/edit";
	}

	@PostMapping("/editCom")
	public String editCom(Member member) {
		System.out.println(
				"===========================================================================" + member.getUser_id());
		repository.saveAndFlush(member);
		// int result = memberDao.updateMember(member);
		// System.out.println(result);
		return "redirect:/";
	}
	@GetMapping("/searchById/{no}")
	public String searchById(@PathVariable int no, Model model) {
		System.out.println("SearchBy ID!!!!     "+no);
		return "index";
	}
}
