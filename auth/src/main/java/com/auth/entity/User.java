package com.auth.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import com.auth.dto.RoleDto;
import com.auth.dto.UserDto;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

	@Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String email;

    private String password;

    private String name;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles=new ArrayList<Role>();
    
    private String societe; 
    

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate = new Date();
    
    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] img;

    @Transactional
    public UserDto getUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setName(name);
        userDto.setEmail(email);
        userDto.setImg(img);
        userDto.setSociete(societe);
        userDto.setCreationDate(creationDate);
        List<RoleDto> roleDtos = roles.stream()
                .map(role -> {
                    RoleDto roleDto = new RoleDto();
                    roleDto.setId(role.getId());
                    roleDto.setName(role.getName());
                    return roleDto;
                })
                .collect(Collectors.toList());
userDto.setRoles(roleDtos);
        return userDto;
    }

}
