package hw11.family.Controller;

import hw11.family.service.FamilyService;
import hw11.family.service.Services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;


public class FamilyController {
    public Services FamilyService;

    public FamilyController(Services service) {
        this.FamilyService = service;
    }

    public void doControl() {
        // проверка метода создания животных и метода получения семьи по ID:
//        FamilyService.addPet(1, new Dog("Dog_Fam1"));
//        FamilyService.addPet(2, new DomesticCat("Cat_Fam2"));
//        FamilyService.addPet(3, new Fish("Fish_Fam3"));
//        FamilyService.getFamilyById(1);
//        FamilyService.getFamilyById(2);
//        FamilyService.getFamilyById(3);


//        FamilyService.deleteFamilyByIndex(1);
//        FamilyService.deleteAllChildrenOlderThen(9);
//        FamilyService.count();


        while (true) {
            Menu.showMenue();
            String choice = Menu.getChoice();
            Pattern pattern = Pattern.compile("q|exit|quit|учше|йгше", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(choice);
            if (matcher.find()) {
                System.out.println("you've quit from application!");
                break;
            }
            boolean skipFlag = Menu.actionConfirmation(choice);
            if (!skipFlag) {
                continue;
            }
//            System.out.println("choice is approved, let's continue...");

            processRequests(choice);
        }
    }

    public void createDefaultFamilies() {

        //исходник с данными по создаваемым семьям:
        List<List<String>> familyData = new ArrayList<>(Arrays.asList(
                Arrays.asList("Leontiy", "Zoya", "Fedotov"),
                Arrays.asList("Petya", "Natasha", "Gandzapety"),
                Arrays.asList("Grigory", "Liza", "Morarik"),
                Arrays.asList("Anton", "Olya", "Glory"),
                Arrays.asList("Pavel", "Tanya", "Primetime"),
                Arrays.asList("Viktor", "Lyuba", "Wonder"),
                Arrays.asList("Marik", "Katya", "Pendik"),
                Arrays.asList("Svyatoslav", "Tonya", "Krutovar"),
                Arrays.asList("Vladik", "Lena", "Mirolyub"),
                Arrays.asList("Sergey", "Nadya", "Rudakov"),
                Arrays.asList("Maks", "Rebekka", "Petrik"),
                Arrays.asList("Vladymir", "Nyura", "Levinsky"),
                Arrays.asList("Yura", "Klavdia", "Shifer"),
                Arrays.asList("Taras", "Marta", "Mirolyub"),
                Arrays.asList("Vovan", "Galya", "Gagik"),
                Arrays.asList("Vertal", "Alla", "Pendiks")
        )
        );

        //задание прочих входных параметров для создания семей:
        Random rnd = new Random();
        int amntOwn, amntAdopted;
        LocalDate dadBirthDate, momBirthDate;

        for (List<String> names : familyData) {
            amntOwn = rnd.nextInt(4);
            amntAdopted = rnd.nextInt(3);
            dadBirthDate = LocalDate.of(1973, 5, 13);
            momBirthDate = dadBirthDate.plusYears(10L).plusMonths(2L).plusDays(13);

            //создание семей и испытании метода createNewFamily():
            FamilyService.createNewFamily(names.get(0), names.get(1), names.get(2),
                    dadBirthDate, momBirthDate, amntOwn, amntAdopted);
        }
    }

    private int getAmtOfMembers() {
        System.out.println("Input number of family members: ");
        return parseInt(hw11.family.service.FamilyService.getKeyboardInput());
    }
    private boolean checkInputInt(int ind, int size){
        if (ind < 0 || ind > size) {
            System.out.println("incorrect number, returning to main menu");
            return false;
        }
        return true;
    }

    public void processRequests(String choice) {
        switch (choice) {
            case "1":
                createDefaultFamilies();
                break;
            case "2":
                FamilyService.displayAllFamilies();
                break;
            case "3":
                FamilyService.getFamiliesBiggerThan(getAmtOfMembers());
                break;
            case "4":
                FamilyService.getFamiliesLessThan(getAmtOfMembers());
                break;
            case "5":
                int searchedMembers = getAmtOfMembers();
                int amntOfFamiliesFound = FamilyService.countFamiliesWithMemberNumber(searchedMembers);
                System.out.println("\t\t\tWe've found " + amntOfFamiliesFound
                        + " families with " + searchedMembers + " family members in them");
                break;
            case "6":

                break;
            case "7":
                System.out.println("Input index of family being deleted:");
                int familiesLeft = FamilyService.count() - 1;
                System.out.println("available range: from 0 to " + (familiesLeft));
                int famIndex = parseInt(hw11.family.service.FamilyService.getKeyboardInput());
                if (!checkInputInt(famIndex, familiesLeft)) {break;}
                FamilyService.deleteFamilyByIndex(famIndex);
                break;
            case "8":
                System.out.println("Input family number:");
                int familiesTotal = FamilyService.count() - 1;
                System.out.println("available range: from 0 to " + (familiesTotal));
                int famNumber = parseInt(hw11.family.service.FamilyService.getKeyboardInput());
                if (!checkInputInt(famNumber, familiesTotal) ) {break;}




                break;
            case "9":
                System.out.println("Input age of children being deleted:");
                int age = parseInt(hw11.family.service.FamilyService.getKeyboardInput());
                FamilyService.deleteAllChildrenOlderThen(age);
                break;
            default:
                System.out.println("больше нет команд");
        }

    }

}
