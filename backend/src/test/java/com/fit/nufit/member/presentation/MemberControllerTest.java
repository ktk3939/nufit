package com.fit.nufit.member.presentation;

import com.fit.nufit.common.ControllerTest;
import com.fit.nufit.member.dto.request.MemberDetailRequest;
import com.fit.nufit.member.dto.response.MemberDetailResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest extends ControllerTest {

    @Test
    void 회원의_상세정보를_조회한다() throws Exception {
        // given
        MemberDetailRequest request = new MemberDetailRequest();

        given(memberService.findDetailBySocialId(any()))
                .willReturn(new MemberDetailResponse());

        // when & then
        mockMvc.perform(get("/api/members/me/details")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(oauth2Login())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 회원의_상세정보를_수정한다() throws Exception {
        // given
        MemberDetailRequest request = new MemberDetailRequest();

        willDoNothing().given(memberService).updateDetailBySocialId(any(), any(MemberDetailRequest.class));

        // when & then
        mockMvc.perform(patch("/api/members/me/details")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                        .with(oauth2Login())
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
