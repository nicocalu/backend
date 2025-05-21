package metier.service;

import com.google.maps.model.LatLng;
import java.io.IOException;
import java.util.List;
import metier.modele.Etablissement;
import util.EducNetApi;
import util.GeoNetApi;

public class ServiceOutil {
    
    /**
     * Permet d'obtenir les informations d'un établissement à partir de son code établissement
     * @param codeEtablissement
     * @return Etablissement : objet représentant l'établissement
     * @throws IOException
     */
    public Etablissement obtenirEtablissementDepuisApi(String codeEtablissement) throws IOException {
        EducNetApi api = new EducNetApi();
        Etablissement etablissement = null;

        List<String> result = api.getInformationEtablissement(codeEtablissement);

        if (result != null) {
            String uai = result.get(0);
            String nom = result.get(1);
            String secteur = result.get(2);
            String codeCommune = result.get(3);
            String nomCommune = result.get(4);
            String codeDepartement = result.get(5);
            String nomDepartement = result.get(6);
            String academie = result.get(7);
            String ips = result.get(8);

            // Adresse pour géolocalisation
            String adresse = nom + ", " + nomCommune + ", " + codeDepartement;
            LatLng coords = GeoNetApi.getLatLng(adresse);

            etablissement = new Etablissement(
                    uai, nom, secteur, codeCommune, nomCommune,
                    nomDepartement, codeDepartement, academie,
                    Double.parseDouble(ips), coords.lat, coords.lng
            );
        }

        return etablissement;
    }

}
