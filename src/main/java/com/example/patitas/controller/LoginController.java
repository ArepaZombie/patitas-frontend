package com.example.patitas.controller;

import com.example.patitas.viewmodel.LoginModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

@RequestMapping("/login")
public class LoginController {
  @GetMapping("/inicio")
  public String inicio(Model model){

    LoginModel loginmodel = new LoginModel("00","","Pepe");
    model.addAttribute("loginmodel",loginmodel);

    return "inicio";
  }

  @PostMapping("/autenticar")
  public String autenticar(@RequestParam("tipodocumento") String tipodocumento,
                           @RequestParam("numerodocumento") String numerodocumento,
                           @RequestParam("password")String password,
                           Model model){

    //Validamos los campos
    if (tipodocumento == null || tipodocumento.trim().length() == 0 ||
    numerodocumento == null || numerodocumento.trim().length() == 0 ||
      password == null || password.trim().length() == 0){
      LoginModel loginmodel = new LoginModel("01","Datos incorrectos","Pepe");
      model.addAttribute("loginmodel",loginmodel);
      return "inicio";
    }


    LoginModel loginmodel = new LoginModel("00","","Jorge Torres");
    model.addAttribute("loginmodel",loginmodel);
    return "principal";
  }

}
