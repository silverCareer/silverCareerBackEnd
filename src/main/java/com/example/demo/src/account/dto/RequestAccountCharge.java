//package com.example.demo.src.account.dto;
//
//import lombok.Builder;
//import org.jetbrains.annotations.NotNull;
//@Builder
//public class RequestAccountCharge (
//        @NotNull
//        String bankName,
//        @NotNull
//        String accountNum,
//        @NotNull
//        Long balance
//){
//}
package com.example.demo.src.account.dto;

import com.example.demo.src.auth.domain.Auth;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Builder
public record RequestAccountCharge(
        @NonNull
        String bankName,
        @NonNull
        String accountNum,
        @NonNull
        Long balance,
        @NonNull
        Long authIdx
) {
}
