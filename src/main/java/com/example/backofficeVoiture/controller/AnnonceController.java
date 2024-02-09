package com.example.backofficeVoiture.controller;

import com.example.backofficeVoiture.form.AnnonceForm;
import com.example.backofficeVoiture.form.AnnonceFormSearch;
import com.example.backofficeVoiture.model.AnnonceUpdateDTO;
import com.example.backofficeVoiture.service.AnnonceService;
import com.example.backofficeVoiture.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/annonce")
public class AnnonceController {

    @Autowired
    AnnonceService annonceService;
    @GetMapping("/{a}/{b}")
    public ApiResponse getAllAnnonce(@PathVariable("a") String a,
                                     @PathVariable("b") String b) {
        return annonceService.obetnirAnnonces(a, b, 20+"");
    }
    @GetMapping
    public ApiResponse getData(@RequestHeader("Authorization") String token) throws AccessDeniedException {
        return annonceService.annonceFormData(token);
    }
    @GetMapping("/{idUser}")
    public ApiResponse getDataByUser(@PathVariable String idUser,@RequestHeader("Authorization") String token){
        System.out.println("annonce par utilisateur");
        return annonceService.obetnirAnnonces(token);
    }
    @PostMapping
    public ApiResponse sendData(@RequestBody AnnonceForm annonceForm,
                                @RequestHeader("Authorization") String token){
        System.out.println("Ampiditra");
        return annonceService.insererAnnonce(annonceForm, token);
    }
    @PutMapping
    public ApiResponse updateAnnonce(@RequestBody AnnonceUpdateDTO annonceUpdateDTO,
                                     @RequestHeader("Authorization") String token){
        return annonceService.updateAnnonce(annonceUpdateDTO, token);
    }
    @DeleteMapping("/{idAnnonce}")
    public void deleteAnnonce(
            @PathVariable(name = "idAnnonce") final String idAnnonce, @RequestHeader("Authorization") String token) throws AccessDeniedException {
        annonceService.delete(idAnnonce, token);
    }
    @GetMapping("/admin/{etat}")
    public ApiResponse getPendingAnnonce(@PathVariable String etat, @RequestHeader("Authorization") String token){
        return annonceService.getPendingAnnonce(etat, token);
    }

    @PutMapping("/admin")
    public ApiResponse valider(@RequestBody AnnonceUpdateDTO annonceUpdateDTO,
                                     @RequestHeader("Authorization") String token){
        return annonceService.validerAnnonce(annonceUpdateDTO, token);
    }

    @PostMapping("/find")
    public ApiResponse rechercheAvance(@RequestBody AnnonceFormSearch annonceFormSearch){
        return annonceService.searchAnnonce(annonceFormSearch);
    }

}
