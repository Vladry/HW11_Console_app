package hw11.family;

import hw11.family.Controller.FamilyController;
import hw11.family.DAO.CollectionFamilyDao;
import hw11.family.People.*;
import hw11.family.service.FamilyService;

public class Main {
    public static void main(String[] args) {

        //создание сервиса DAO:
        CollectionFamilyDao<Family> familyMemStorage = new CollectionFamilyDao<>();//создаём хранилище1
        FamilyService service = new FamilyService(familyMemStorage);
        FamilyController controller = new FamilyController(service);

        controller.doControl();
    }
}



