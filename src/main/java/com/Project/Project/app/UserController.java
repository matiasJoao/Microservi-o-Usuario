package com.Project.Project.app;

import com.Project.Project.Business.UserService;
import com.Project.Project.DataAcess.User;
import com.Project.Project.ResponseHandler.ResponseJSONhandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping
    @RequestMapping("/user/cadastro")
    public ResponseEntity cadastro(@RequestBody @Valid User user) {
        String email = user.getEmail();
        String nome = user.getName();
        String cpf = user.getCpf();
        String senha = user.getSenha();
        return userService.verifyEmailCpfNameSenha(email, cpf, nome, senha, user);
    }

    @GetMapping
    @RequestMapping("/users-list")
    @ResponseStatus(HttpStatus.OK)
    public List<User> ListUser(){
        return userService.ListClients();
    }
    @GetMapping
    @RequestMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User FindId(@PathVariable Long id){
        return userService.ListUniqClient(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
    }

    @GetMapping
    @RequestMapping("/user/login/{email}/password/{senha}")
    public ResponseEntity loginUser(@PathVariable("email") String email, @PathVariable("senha") String senha ){
         return userService.loginClient(email, senha);
    }
    @PutMapping("/user/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User Update(@PathVariable("id") Long id, @RequestBody User user) {
        String pass;
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        pass = passwordEncoder.encode(user.getSenha());
        user.setSenha(pass);
        return userService.UpdateById(id, user);
    }
    @PatchMapping("/user/updateEmail/{id}/{email}")
    public User UpdateEmail(@PathVariable("id") Long id, @PathVariable("email") String email){
        User user = FindId(id);
        user.setEmail(email);
        return userService.save(user);
    }
    @DeleteMapping("/user/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseJSONhandler delete (@PathVariable Long id){
        FindId(id);
        return userService.delete(id);
    }


}
