package com.ood.tamanage.controller;

import com.ood.tamanage.enums.Evaluation;
import com.ood.tamanage.enums.Grade;
import com.ood.tamanage.enums.Status;
import com.ood.tamanage.enums.Trimester;
import com.ood.tamanage.pojo.*;
import com.ood.tamanage.pojo.Module;
import com.ood.tamanage.service.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/lecturers")
public class LecturerController {
    final static Trimester trimester = Trimester.Autumn;
    @Resource
    private LecturerService lecturerService;

    @Resource
    private StudentService studentService;

    @Resource
    private MajorService majorService; // 注入MajorService

    @Resource
    private CourseService courseService;
    @Resource
    private ModuleService moduleService;

    @Resource
    private ApplicationService applicationService;

    @Resource
    private OfferService offerService;
    @Resource
    private RequirementService requirementService;

    @Resource
    private PositionService positionService;

    @GetMapping("/index_lecturer")
    public String showLecturerIndexPage(HttpSession session, Model model) {
        Lecturer lecturer = (Lecturer) session.getAttribute("user");
        if (lecturer != null) {
            model.addAttribute("lecturer", lecturer);
        } else {
            return "login";
        }
        return "lecturer/index_lecturer"; // 这里返回的是视图名称
    }

    @GetMapping("/requirements")
    public String showRequirement(HttpSession session, Model model) {
        Lecturer lecturer = (Lecturer) session.getAttribute("user");
        if (lecturer != null) {
            model.addAttribute("lecturer", lecturer);
            List<Course> labCourses = courseService.getCoursesLabListByUid(lecturer.getUid());
            List<Module> labModules = moduleService.getModulesLabListByUid(lecturer.getUid());
            List<Requirements> requirementsList = new ArrayList<>();
            for (Course course : labCourses) {
                Requirements requirements = requirementService.getRequirementsByCID(course.getCid());
                Course course1 = courseService.getCourseByCID(course.getCid());
                requirements.setCode(course1.getCourseCode());
                requirementsList.add(requirements);
            }
            for (Module module : labModules) {
                Requirements requirements = requirementService.getRequirementsByMID(module.getMid());
                Module module1 = moduleService.getModuleByMID(module.getMid());
                requirements.setCode(module1.getModuleCode());
                requirementsList.add(requirements);
            }
            lecturer.setRequirementsList(requirementsList);
            model.addAttribute("requirementsList", requirementsList);
        } else {
            return "login";
        }
        return "lecturer/courses_lecturer"; // 这里返回的是视图名称
    }

    @ResponseBody
    @PutMapping("/requirements/management")
    public Result manageRequirements(@RequestBody Map<String, Object> information) {
        try {
            Requirements requirements = new Requirements();
            requirements.setRid(Integer.parseInt(information.get("rid").toString()));
            requirements.setQuantity(Integer.parseInt(information.get("numberneed").toString()));
            requirements.setWorkHours(Integer.parseInt(information.get("times").toString()));
            requirements.setClassGrade(Grade.valueOf(information.get("gradeStatus").toString()));
            requirements.setStageRequirement(Integer.parseInt(information.get("stageLimit").toString()));
            requirements.setMaster(Boolean.parseBoolean(information.get("master").toString()));
            requirements.setUndergrad(Boolean.parseBoolean(information.get("undergrad").toString()));
            requirementService.updateRequirements(requirements);
            return Result.success("update requirement successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("update requirement failed");
        }
    }

    @GetMapping("/offers")
    public String sendOfferPage(HttpSession session, Model model) {
        Lecturer lecturer = (Lecturer) session.getAttribute("user");
        if (lecturer != null) {
            model.addAttribute("lecturer", lecturer);
            List<Course> labCourses = courseService.getCoursesLabListByUid(lecturer.getUid());
            List<Module> labModules = moduleService.getModulesLabListByUid(lecturer.getUid());
            List<Requirements> requirementsList = new ArrayList<>();
            for (Course course : labCourses) {
                Requirements requirements = requirementService.getRequirementsByCID(course.getCid());
                Course course1 = courseService.getCourseByCID(course.getCid());
                requirements.setCode(course1.getCourseCode());
                requirementsList.add(requirements);
            }
            for (Module module : labModules) {
                Requirements requirements = requirementService.getRequirementsByMID(module.getMid());
                Module module1 = moduleService.getModuleByMID(module.getMid());
                requirements.setCode(module1.getModuleCode());
                requirementsList.add(requirements);
            }
            lecturer.setRequirementsList(requirementsList);
            model.addAttribute("requirementsList", requirementsList);
        } else {
            return "login";
        }
        return "lecturer/send_offer"; // 这里返回的是视图名称
    }

    @ResponseBody
    @PostMapping("/sendOffer")
    public Result sendOffer(@RequestBody Map<String, Object> information) {
        try {
            Offers offer = new Offers();
            Course course = new Course();
            Module module = new Module();
            if (!Boolean.parseBoolean(information.get("cm").toString())) {
                course = courseService.getCourseByCID(Integer.parseInt(information.get("id").toString()));
                offer.setCid(Integer.parseInt(information.get("id").toString()));
            } else {
                module = moduleService.getModuleByMID(Integer.parseInt(information.get("id").toString()));
                offer.setMid(Integer.parseInt(information.get("id").toString()));
            }
            offer.setDescription(information.get("description").toString());
            offer.setStatus(Status.valueOf("Pending"));
            Student student = studentService.getStudentByStudentId(information.get("studentId").toString());
            if (student == null) {
                return Result.error("Student not found");
            }
            if (offer.getCid() != 0) {
                if (studentService.applyForCourse(student.getUid(), offer.getCid())){
                    return Result.error("Student has been applied");
                }
                if (offerService.hasStuedentOfferByCID(offer.getCid(), student.getUid())) {
                    return Result.error("Student has been offered");
                }
                if (!Objects.equals(student.getMajorCode(), course.getMajorCode())) {
                    return Result.error("Student major not match");
                }
                if (student.getStage() < course.getStage() && student.getEducationStatus().name().equals("UNDERGRAD")) {
                    return Result.error("Student stage not match");
                }
                offerService.addOfferByCID(offer, student.getUid());
            } else {
                if (studentService.applyForModule(student.getUid(), offer.getMid())){
                    return Result.error("Student has been applied");
                }
                if (offerService.hasStuedentOfferByMID(offer.getMid(), student.getUid())) {
                    return Result.error("Student has been offered");
                }
                List<MajorModule> majors = moduleService.getMajorsForModule(module);
                List<String> majorsForModuleList = new ArrayList<>();
                for (MajorModule majorModule : majors) {
                    majorsForModuleList.add(majorModule.getMajorCode());
                }
                if (!majorsForModuleList.contains(student.getMajorCode())) {
                    return Result.error("Student major not match");
                }
                if (student.getStage() < module.getStage() && student.getEducationStatus().name().equals("UNDERGRAD")) {
                    return Result.error("Student stage not match");
                }
                offerService.addOfferByMID(offer, student.getUid());
            }
            return Result.success("Offer send successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("Offer send failed");
        }
    }

    @GetMapping("/viewOfferApp")
    public String viewOfferApp(HttpSession session, Model model) {
        Lecturer lecturer = (Lecturer) session.getAttribute("user");
        if (lecturer != null) {
            model.addAttribute("lecturer", lecturer);
            List<Course> labCourses = courseService.getCoursesLabListByUid(lecturer.getUid());
            List<Module> labModules = moduleService.getModulesLabListByUid(lecturer.getUid());
            List<Offers> offersList = new ArrayList<>();
            List<Applications> applicationsList = new ArrayList<>();
            List<Applications> applicationsList2 = new ArrayList<>();
            for (Course course : labCourses) {
                List<Offers> offers = offerService.getOffersByCID(course.getCid());
                if (!offers.isEmpty()){
                    for (Offers offer:offers) {
                        Student student = studentService.getStudentById(offer.getUid());
                        offer.setSid(student.getStudentId());
                        offer.setUsername(student.getUsername());
                        offer.setCode(course.getCourseCode());
                        offersList.add(offer);
                    }
                }
                List<Applications> applications = applicationService.getApplicationsByCID(course.getCid());
                for (Applications application : applications) {
                    if (application != null && application.getStatus().name().equals("AdminAccepted")) {
                        Student student = studentService.getStudentById(application.getUid());
                        application.setStudent(student);
                        application.setCode(course.getCourseCode());
                        applicationsList.add(application);
                    }
                    if (application != null && application.getStatus().name().equals("Alternative")) {
                        Student student = studentService.getStudentById(application.getUid());
                        application.setStudent(student);
                        application.setCode(course.getCourseCode());
                        applicationsList2.add(application);
                    }
                }

            }
            for (Module module : labModules) {
                List<Offers> offers = offerService.getOffersByMID(module.getMid());
                if (!offers.isEmpty()){
                    for (Offers offer:offers) {
                        Student student = studentService.getStudentById(offer.getUid());
                        offer.setSid(student.getStudentId());
                        offer.setUsername(student.getUsername());
                        offer.setCode(module.getModuleCode());
                        offersList.add(offer);
                    }
                }
                List<Applications> applications = applicationService.getApplicationsByMID(module.getMid());
                for (Applications application : applications) {
                    if (application != null && application.getStatus().name().equals("AdminAccepted")) {
                        Student student = studentService.getStudentById(application.getUid());
                        application.setStudent(student);
                        application.setCode(module.getModuleCode());
                        applicationsList.add(application);
                    }
                    if (application != null && application.getStatus().name().equals("Alternative")) {
                        Student student = studentService.getStudentById(application.getUid());
                        application.setStudent(student);
                        application.setCode(module.getModuleCode());
                        applicationsList2.add(application);
                    }
                }
            }
            model.addAttribute("offersList", offersList);
            model.addAttribute("applicationsList", applicationsList);
            model.addAttribute("applicationsList2", applicationsList2);

        } else {
            return "login";
        }
        return "lecturer/viewOfferAndApp"; // 这里返回的是视图名称
    }

    @GetMapping("/applications/{aid}")
    @ResponseBody
    public Applications getApplicationById(@PathVariable int aid) {
        Applications applications = applicationService.getApplicationById(aid);
        Student student = studentService.getStudentById(applications.getUid());
        applications.setStudent(student);
        return applications;
    }

    @ResponseBody
    @PutMapping("/putApplications/{uid}/{aid}")
    public Result putApplication(@PathVariable int uid, @PathVariable int aid, @RequestParam String status) {
        try {
            Status applicationStatus = Status.valueOf(status);
            applicationService.updateApplication(aid, applicationStatus);
            if (applicationStatus.equals(Status.Rejected)) {
                return Result.success("application rejected");
            }
            if (applicationStatus.equals(Status.Alternative)) {
                return Result.success("application alternative");
            }
            Applications applications = applicationService.getApplicationById(aid);
            if (applications.getCid() != 0) {
                positionService.addPositionByCid(uid, applications.getCid());
            } else {
                positionService.addPositionByMid(uid, applications.getMid());
            }
            return Result.success("application accepted");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("application failed");
        }
    }

    @GetMapping("/positions")
    public String lecturerPositionsPage(HttpSession session, Model model){
        Lecturer lecturer = (Lecturer) session.getAttribute("user");
        if (lecturer != null) {
            model.addAttribute("lecturer", lecturer);
            List<Positions> positionsList = positionService.getPositionsList();
            List<Positions> positionsListShow = new ArrayList<>();
            List<Course> labCourses = courseService.getCoursesLabListByUid(lecturer.getUid());
            List<Module> labModules = moduleService.getModulesLabListByUid(lecturer.getUid());
            List<Integer> labCoursesId = new ArrayList<>();
            List<Integer> labModulesId = new ArrayList<>();
            for (Course course : labCourses) {
                labCoursesId.add(course.getCid());
            }
            for (Module module : labModules) {
                labModulesId.add(module.getMid());
            }
            for (Positions position : positionsList) {
                if (labCoursesId.contains(position.getCid()) || labModulesId.contains(position.getMid())) {
                    Student student = studentService.getStudentById(position.getUid());
                    if (position.getCid() != 0) {
                        Course course = courseService.getCourseByCID(position.getCid());
                        position.setCode(course.getCourseCode());
                    } else {
                        Module module = moduleService.getModuleByMID(position.getMid());
                        position.setCode(module.getModuleCode());
                    }
                    position.setStudent(student);
                    positionsListShow.add(position);
                }
            }
            model.addAttribute("positionsList", positionsListShow);
        } else {
            return "login";
        }
        return "lecturer/view_positions"; // 这里返回的是视图名称
    }

    @ResponseBody
    @PutMapping("/putPositions/{pid}")
    public Result putPositions(@PathVariable int pid, @RequestParam String evaluation) {
        try {
            Evaluation evaluation1 = Evaluation.valueOf(evaluation);
            positionService.updatePosition(pid, evaluation1);
            return Result.success("position evaluation updated");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("position evaluation update failed");
        }
    }

    @GetMapping("/timetable")
    public String viewTimetable(HttpSession session, Model model){
        Lecturer lecturer = (Lecturer) session.getAttribute("user");
        if (lecturer != null) {
            model.addAttribute("lecturer", lecturer);
            List<Course> courses = courseService.getCoursesListByUidByTrimester(lecturer.getUid(), trimester);
            List<Module> modules = moduleService.getModulesListByUidByTrimester(lecturer.getUid(), trimester);
            model.addAttribute("courses", courses);
            model.addAttribute("modules", modules);
        } else {
            return "login";
        }
        return "lecturer/timetable_l"; // 这里返回的是视图名称
    }

    @ResponseBody
    @GetMapping
    public List<Lecturer> getLecturersList() {
        return lecturerService.getLecturersList();
    }

    @ResponseBody
    @GetMapping("/{uid}")
    public Lecturer getLecturerById(@PathVariable int uid) {
        return lecturerService.getLecturerById(uid);
    }

    @ResponseBody
    @PostMapping
    public void addLecturer(Lecturer lecturer) {
        lecturerService.addLecturer(lecturer);
    }

    @ResponseBody
    @PutMapping
    public void updateLecturer(Lecturer lecturer) {
        lecturerService.updateLecturer(lecturer);
    }

    @ResponseBody
    @DeleteMapping("/{uid}")
    public void deleteLecturer(@PathVariable int uid) {
        lecturerService.deleteLecturer(uid);
    }
}
