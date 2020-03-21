package com.voterapp.voter.vapp.model;

import com.voterapp.voter.vapp.util.DateAudit;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;


@EqualsAndHashCode(callSuper = true)
@Entity(name = "users")
@Table(
        name = "users",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"username", "email"})
)
@Data
@Accessors(chain = true)
public class User extends DateAudit implements Serializable {

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private static final long serialVersionUID = -3204603234453030769L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank
    @NaturalId
    @Size(max = 15, min = 1)
    private String username;

    @NotBlank
    @Size(min = 1, max = 40)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 40)
    private String lastName;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    private String encryptedPassword;

}
