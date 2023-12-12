package com.softuni.angelovestates;

import com.softuni.angelovestates.model.entity.*;
import com.softuni.angelovestates.model.enums.OfferTypeEnum;
import com.softuni.angelovestates.model.enums.ProvinceEnum;
import com.softuni.angelovestates.model.enums.RoleEnum;
import com.softuni.angelovestates.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class InitDB {

    private final RoleRepository roleRepository;
    private final ProvinceRepository provinceRepository;
    private final OfferTypeRepository offerTypeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReviewRepository reviewRepository;
    private final OfferRepository offerRepository;

    @Autowired
    public InitDB(RoleRepository roleRepository, ProvinceRepository provinceRepository, OfferTypeRepository offerTypeRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, ReviewRepository reviewRepository, OfferRepository offerRepository) {
        this.roleRepository = roleRepository;
        this.provinceRepository = provinceRepository;
        this.offerTypeRepository = offerTypeRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.reviewRepository = reviewRepository;
        this.offerRepository = offerRepository;
    }

    @PostConstruct
    void init() {

        if(this.roleRepository.count() == 0) initRoles();
        if(this.provinceRepository.count() == 0) initProvinces();
        if(this.offerTypeRepository.count() == 0) initOfferTypes();
        if(this.userRepository.count() == 0) initUsers();
    }

    public void initRoles() {
        for (RoleEnum value : RoleEnum.values()) {
            this.roleRepository.save(new Role().setRole(value));
        }
    }

    public void initProvinces(){
        for (ProvinceEnum value : ProvinceEnum.values()) {
            this.provinceRepository.save(new Province().setProvince(value));
        }
    }

    public void initOfferTypes() {
        for (OfferTypeEnum value : OfferTypeEnum.values()) {
            this.offerTypeRepository.save(new OfferType().setOfferType(value));
        }
    }

    public void initUsers(){
        initAdmin();
        initUser();
        initAgents();
        initReviews();
        initOffers();
    }

    public void initUser() {
        User user = new User()
                .setEmail("user@gmail.com")
                .setFirstName("John")
                .setLastName("Smith")
                .setPhoneNumber("0878604426")
                .setAgent(false)
                .setPassword(passwordEncoder.encode("user123"));
        user.getRoles().add(this.roleRepository.getByRole(RoleEnum.USER));
        this.userRepository.saveAndFlush(user);
    }

    public void initAdmin() {

        User admin= new User()
                .setEmail("admin@gmail.com")
                .setFirstName("Admin")
                .setLastName("Adminov")
                .setPhoneNumber("0878604227")
                .setAgent(false)
                .setPassword(passwordEncoder.encode("admin123"));

        admin.getRoles().add(this.roleRepository.getByRole(RoleEnum.USER));
        admin.getRoles().add(this.roleRepository.getByRole(RoleEnum.ADMIN));

        this.userRepository.saveAndFlush(admin);
    }

    public void initAgents() {
        User firstAgent = new User()
                .setEmail("javadeveloper@gmail.com")
                .setFirstName("James")
                .setLastName("Gosling")
                .setCompany("Coffee Agency")
                .setPhoneNumber("034949494")
                .setPhotoURL("https://dri.es/files/images/blog/james-gosling.jpg")
                .setAgent(true)
                .setPassword(this.passwordEncoder.encode("agent123"));
        firstAgent.getRoles().add(this.roleRepository.getByRole(RoleEnum.USER));
        firstAgent.getRoles().add(this.roleRepository.getByRole(RoleEnum.AGENT));

        User secondAgent = new User()
                .setEmail("bjarne@gmail.com")
                .setFirstName("Bjarne")
                .setLastName("Stroustrup")
                .setCompany("Awesome agency")
                .setPhoneNumber("034949794")
                .setPhotoURL("https://ieeecs-media.computer.org/wp-media/2018/04/06220832/bjarnestroustrup.jpg")
                .setAgent(true)
                .setPassword(this.passwordEncoder.encode("agent123"));
        secondAgent.getRoles().add(this.roleRepository.getByRole(RoleEnum.USER));
        secondAgent.getRoles().add(this.roleRepository.getByRole(RoleEnum.AGENT));

        User thirdAgent = new User()
                .setEmail("ccreator@gmail.com")
                .setFirstName("Dennis")
                .setLastName("Ritchie")
                .setCompany("CodingEstates")
                .setPhoneNumber("034999494")
                .setPhotoURL("https://upload.wikimedia.org/wikipedia/commons/thumb/2/23/Dennis_Ritchie_2011.jpg/220px-Dennis_Ritchie_2011.jpg")
                .setAgent(true)
                .setPassword(this.passwordEncoder.encode("agent123"));
        thirdAgent.getRoles().add(this.roleRepository.getByRole(RoleEnum.USER));
        thirdAgent.getRoles().add(this.roleRepository.getByRole(RoleEnum.AGENT));

        this.userRepository.saveAllAndFlush(List.of(firstAgent, secondAgent, thirdAgent));
    }

    public void initOffers() {

        Offer offer1 = new Offer()
                .setTitle("Awesome house")
                .setOfferType(this.offerTypeRepository.findByOfferType(OfferTypeEnum.FOR_SALE))
                .setPrice(BigDecimal.valueOf(100000))
                .setSize(250)
                .setRooms(7)
                .setDescription("Awesome house with 7 rooms. Nice for family with kids! If you are interested call me.")
                .setProvince(this.provinceRepository.getProvinceByProvince(ProvinceEnum.DOBRICH))
                .setAddress("Ul. Roza, No 122")
                .setListedOn(LocalDate.now())
                .setIsExpired(false)
                .setSeller(this.userRepository.findUserByEmail("user@gmail.com").get());
        offer1.getPhotoURLs().add("https://res.cloudinary.com/dyfbmcq8q/image/upload/v1701375812/snimka1_n9jdn7.jpg");
        offer1.getPhotoURLs().add("https://res.cloudinary.com/dyfbmcq8q/image/upload/v1701375812/snimka3_j8fbj3.jpg");
        offer1.getPhotoURLs().add("https://res.cloudinary.com/dyfbmcq8q/image/upload/v1701375812/snimka3_j8fbj3.jpg");

        Offer offer2 = new Offer()
                .setTitle("Nice apartment")
                .setOfferType(this.offerTypeRepository.findByOfferType(OfferTypeEnum.FOR_SALE))
                .setPrice(BigDecimal.valueOf(150000))
                .setSize(120)
                .setRooms(4)
                .setDescription("The best apartment in the town. 5min away from the beach.")
                .setProvince(this.provinceRepository.getProvinceByProvince(ProvinceEnum.VARNA))
                .setAddress("Ul. Kokiche, No 122")
                .setListedOn(LocalDate.now())
                .setIsExpired(false)
                .setSeller(this.userRepository.findUserByEmail("user@gmail.com").get());
        offer2.getPhotoURLs().add("https://res.cloudinary.com/dyfbmcq8q/image/upload/v1701375940/zaglav_gvwiuh.jpg");
        offer2.getPhotoURLs().add("https://res.cloudinary.com/dyfbmcq8q/image/upload/v1701375952/60887_12457_IMG_04_0000_jzvadl.jpg");
        offer2.getPhotoURLs().add("https://res.cloudinary.com/dyfbmcq8q/image/upload/v1701375953/60887_12457_IMG_05_0000_b7ujth.jpg");

        Offer offer3 = new Offer()
                .setTitle("Luxurious family house")
                .setOfferType(this.offerTypeRepository.findByOfferType(OfferTypeEnum.FOR_SALE))
                .setPrice(BigDecimal.valueOf(2000000))
                .setSize(670)
                .setRooms(12)
                .setDescription("Luxurious family house for the summer. Large pool.")
                .setProvince(this.provinceRepository.getProvinceByProvince(ProvinceEnum.BURGAS))
                .setAddress("Ul. Prof. Georgi Georgiev, No 1")
                .setListedOn(LocalDate.now())
                .setIsExpired(false)
                .setSeller(this.userRepository.findUserByEmail("user@gmail.com").get());
        offer3.getPhotoURLs().add("https://res.cloudinary.com/dyfbmcq8q/image/upload/v1701376246/52214_VL3601_IMG_02_0000_ncwj80.jpg");
        offer3.getPhotoURLs().add("https://res.cloudinary.com/dyfbmcq8q/image/upload/v1701376159/52214_VL3601_IMG_01_0000_wxxajd.jpg");

        Offer offer4 = new Offer()
                .setTitle("Small cozy house")
                .setOfferType(this.offerTypeRepository.findByOfferType(OfferTypeEnum.FOR_SALE))
                .setPrice(BigDecimal.valueOf(25000))
                .setSize(670)
                .setRooms(5)
                .setDescription("Small cozy house. 5 rooms. For more informatoin please call.")
                .setProvince(this.provinceRepository.getProvinceByProvince(ProvinceEnum.VELIKO_TARNOVO))
                .setAddress("Ul. Ivan Vazov, No 1")
                .setListedOn(LocalDate.now())
                .setIsExpired(false)
                .setSeller(this.userRepository.findUserByEmail("user@gmail.com").get());
        offer4.getPhotoURLs().add("https://res.cloudinary.com/dyfbmcq8q/image/upload/v1701376497/90247_SF2902_IMG_04_0000_c8ui74.jpg");
        offer4.getPhotoURLs().add("https://res.cloudinary.com/dyfbmcq8q/image/upload/v1701376497/90247_SF2902_IMG_00_0000_tycrfa.jpg");

        Offer offer5 = new Offer()
                .setTitle("Just a regular apartment")
                .setOfferType(this.offerTypeRepository.findByOfferType(OfferTypeEnum.FOR_SALE))
                .setPrice(BigDecimal.valueOf(55000))
                .setSize(75)
                .setRooms(3)
                .setDescription("Just a regular apartment with 3 rooms. Nice view from the terrace.")
                .setProvince(this.provinceRepository.getProvinceByProvince(ProvinceEnum.LOVECH))
                .setAddress("Ul. Targovska No 112")
                .setListedOn(LocalDate.now())
                .setIsExpired(true)
                .setSeller(this.userRepository.findUserByEmail("user@gmail.com").get());
        offer5.getPhotoURLs().add("https://res.cloudinary.com/dyfbmcq8q/image/upload/v1701376785/263276_9507_IMG_03_0000_mfhla6.jpg");
        offer5.getPhotoURLs().add("https://res.cloudinary.com/dyfbmcq8q/image/upload/v1701376786/263276_9507_IMG_04_0000_zckxqf.jpg");

        Offer offer6 = new Offer()
                .setTitle("Nice apartment")
                .setOfferType(this.offerTypeRepository.findByOfferType(OfferTypeEnum.FOR_SALE))
                .setPrice(BigDecimal.valueOf(150000))
                .setSize(120)
                .setRooms(4)
                .setDescription("The best apartment in the town. 5min away from the beach.")
                .setProvince(this.provinceRepository.getProvinceByProvince(ProvinceEnum.VARNA))
                .setAddress("Ul. Kokiche, No 122")
                .setListedOn(LocalDate.now())
                .setIsExpired(false)
                .setSeller(this.userRepository.findUserByEmail("user@gmail.com").get());
        offer2.getPhotoURLs().add("https://res.cloudinary.com/dyfbmcq8q/image/upload/v1701375940/zaglav_gvwiuh.jpg");
        offer2.getPhotoURLs().add("https://res.cloudinary.com/dyfbmcq8q/image/upload/v1701375952/60887_12457_IMG_04_0000_jzvadl.jpg");
        offer2.getPhotoURLs().add("https://res.cloudinary.com/dyfbmcq8q/image/upload/v1701375953/60887_12457_IMG_05_0000_b7ujth.jpg");

        Offer offer7 = new Offer()
                .setTitle("Luxurious family house")
                .setOfferType(this.offerTypeRepository.findByOfferType(OfferTypeEnum.FOR_SALE))
                .setPrice(BigDecimal.valueOf(2000000))
                .setSize(670)
                .setRooms(12)
                .setDescription("Luxurious family house for the summer. Large pool.")
                .setProvince(this.provinceRepository.getProvinceByProvince(ProvinceEnum.BURGAS))
                .setAddress("Ul. Prof. Georgi Georgiev, No 1")
                .setListedOn(LocalDate.now())
                .setIsExpired(false)
                .setSeller(this.userRepository.findUserByEmail("user@gmail.com").get());
        offer3.getPhotoURLs().add("https://res.cloudinary.com/dyfbmcq8q/image/upload/v1701376246/52214_VL3601_IMG_02_0000_ncwj80.jpg");
        offer3.getPhotoURLs().add("https://res.cloudinary.com/dyfbmcq8q/image/upload/v1701376159/52214_VL3601_IMG_01_0000_wxxajd.jpg");

        Offer offer8 = new Offer()
                .setTitle("Small cozy house")
                .setOfferType(this.offerTypeRepository.findByOfferType(OfferTypeEnum.FOR_SALE))
                .setPrice(BigDecimal.valueOf(25000))
                .setSize(670)
                .setRooms(5)
                .setDescription("Small cozy house. 5 rooms. For more informatoin please call.")
                .setProvince(this.provinceRepository.getProvinceByProvince(ProvinceEnum.VELIKO_TARNOVO))
                .setAddress("Ul. Ivan Vazov, No 1")
                .setListedOn(LocalDate.now())
                .setIsExpired(false)
                .setSeller(this.userRepository.findUserByEmail("user@gmail.com").get());
        offer4.getPhotoURLs().add("https://res.cloudinary.com/dyfbmcq8q/image/upload/v1701376497/90247_SF2902_IMG_04_0000_c8ui74.jpg");
        offer4.getPhotoURLs().add("https://res.cloudinary.com/dyfbmcq8q/image/upload/v1701376497/90247_SF2902_IMG_00_0000_tycrfa.jpg");

        this.offerRepository.saveAllAndFlush(List.of(offer1, offer2, offer3, offer4, offer5, offer6, offer7, offer8));
    }

    public void initReviews() {
        Review firstReview = new Review()
                .setAuthor(this.userRepository.findUserByEmail("bjarne@gmail.com").get())
                .setRating(5)
                .setComment("AngelovEstates is the best website for selling properties!");

        Review secondReview = new Review()
                .setAuthor(this.userRepository.findUserByEmail("javadeveloper@gmail.com").get())
                .setRating(5)
                .setComment("The site is awesome. Even more awesome is that it is written in Java!!!");

        Review thirdReview = new Review()
                .setAuthor(this.userRepository.findUserByEmail("user@gmail.com").get())
                .setRating(3)
                .setComment("The site is well-structured, but needs more features!");

        this.reviewRepository.saveAllAndFlush(List.of(firstReview, secondReview, thirdReview));
    }

}
