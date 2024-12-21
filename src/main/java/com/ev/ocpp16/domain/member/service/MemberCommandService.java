package com.ev.ocpp16.domain.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ev.ocpp16.domain.member.entity.Member;
import com.ev.ocpp16.domain.member.exception.DuplicateMemberException;
import com.ev.ocpp16.domain.member.exception.InvalidMemberException;
import com.ev.ocpp16.domain.member.exception.MemberException;
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
	 * @throws MemberException
	 */
	public Member saveMember(Member member) throws MemberException {
		if (member == null) {
			throw new MemberException("회원 정보가 올바르지 않습니다.");
		}

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
	 * @throws InvalidMemberException
	 * @throws DuplicateMemberException
	 */
	public void validateMemberSave(String idToken, String email, String phoneNumber, String carNumber)
			throws InvalidMemberException, DuplicateMemberException {

		if (!StringUtils.hasText(idToken) || !StringUtils.hasText(email) || !StringUtils.hasText(phoneNumber)
				|| !StringUtils.hasText(carNumber)) {
			throw new InvalidMemberException("입력값이 올바르지 않습니다.");
		}

		if (idToken.length() != 36 || email.length() > 100 || phoneNumber.length() > 15 || carNumber.length() > 15) {
			throw new InvalidMemberException("입력값이 올바르지 않습니다.");
		}

		if (memberRepository.existsByIdToken(idToken)) {
			throw new DuplicateMemberException("중복된 토큰입니다.");
		}

		if (memberRepository.existsByEmail(email)) {
			throw new DuplicateMemberException("중복된 이메일입니다.");
		}

		if (memberRepository.existsByPhoneNumber(phoneNumber)) {
			throw new DuplicateMemberException("중복된 전화번호입니다.");
		}

		if (memberRepository.existsByCarNumber(carNumber)) {
			throw new DuplicateMemberException("중복된 차량번호입니다.");
		}
	}
}