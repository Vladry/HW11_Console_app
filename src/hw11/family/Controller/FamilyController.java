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


//        FamilyService.countFamiliesWithMemberNumber(4);
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
            System.out.println("choice is approved, let's continue...");

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

    public void processRequests(String choice) {
        switch (choice) {
            case "1":
                createDefaultFamilies();
                break;
            case "2":
                FamilyService.displayAllFamilies();
                break;
            case "3":
                System.out.println("Input number of family members: ");
                String inp = hw11.family.service.FamilyService.getKeyboardInput();
//                String inp = FamilyService.getKeyboardInput();
                int amount = parseInt(inp);
                FamilyService.getFamiliesBiggerThan(amount);
                break;
            case "4":
                System.out.println("Input number of family members: ");
                String inp2 = hw11.family.service.FamilyService.getKeyboardInput();
//                String inp2 = FamilyService.getKeyboardInput();
                int amount2 = parseInt(inp2);
                FamilyService.getFamiliesLessThan(amount2);
                break;

            case "5":

                break;
            case "6":

                break;
            case "7":

                break;
            case "8":

                break;
            case "9":

                break;
            default:
                System.out.println("больше нет команд");
        }

    }

}
