package net.dreamfteam.quiznet.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JWTLoginSuccessResponse {
   private boolean success;
   private String token;

   public JWTLoginSuccessResponse(boolean success, String token) {
      this.success = success;
      this.token = token;
   }

   @Override
   public String toString() {
      return "JWTLoginSuccessResponse{" +
              "success=" + success +
              ", token='" + token + '\'' +
              '}';
   }
}
