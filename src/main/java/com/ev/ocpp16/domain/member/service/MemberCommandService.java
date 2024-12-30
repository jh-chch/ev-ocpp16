package com.ev.ocpp16.domain.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ev.ocpp16.domain.member.entity.Member;
import com.ev.ocpp16.domain.member.exception.MemberDuplicateException;
import com.ev.ocpp16.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * 회원 등록
	 * 
	 * @param member 회원
	 * @return 저장된 회원
	 * @throws MemberDuplicateException
	 */
	public Member saveMember(Member member) throws MemberDuplicateException {
		validateMemberSave(member.getIdToken(), member.getEmail(), member.getPhoneNumber(), member.getCarNumber());
		member.changeEncodedPassword(passwordEncoder.encode(member.getPassword()));
		return memberRepository.save(member);
	}

	/**
	 * 회원 등록 전 유효성 검사
	 * 
	 * @param idToken
	 * @param email
	 * @param phoneNumber
	 * @param carNumber
	 * @throws MemberDuplicateException
	 */
	private void validateMemberSave(String idToken, String email, String phoneNumber, String carNumber)
			throws IllegalArgumentException {
		if (memberRepository.existsByIdToken(idToken)) {
			throw new MemberDuplicateException("중복된 토큰입니다.");
		}

		if (memberRepository.existsByEmail(email)) {
			throw new MemberDuplicateException("중복된 이메일입니다.");
		}

		if (memberRepository.existsByPhoneNumber(phoneNumber)) {
			throw new MemberDuplicateException("중복된 전화번호입니다.");
		}

		if (memberRepository.existsByCarNumber(carNumber)) {
			throw new MemberDuplicateException("중복된 차량번호입니다.");
		}
	}
}