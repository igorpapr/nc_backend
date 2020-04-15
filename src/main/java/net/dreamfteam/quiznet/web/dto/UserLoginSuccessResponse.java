package net.dreamfteam.quiznet.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@Getter
@Setter
@Builder
public class UserLoginSuccessResponse {

   private boolean success;
   private String token;
   private String username;
   private String email;
   private Date creationDate;

   public UserLoginSuccessResponse(boolean success, String token, String username, String email, Date creationDate) {
      this.success = success;
      this.token = token;
      this.username = username;
      this.email = email;
      this.creationDate = creationDate;
   }
   
}
