// AdminController.java
package com.ood.tamanage.controller;

import com.ood.tamanage.enums.*;
import com.ood.tamanage.pojo.*;
import com.ood.tamanage.pojo.Module;
import com.ood.tamanage.service.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admins")
public class AdminController {

    @Resource
    private AdminService adminService;

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
    @Resource
    private ClassModifyService classModifyService;

    // 首页
    @GetMapping("/index_admin")
    public String showAdminIndexPage(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("user");
        if (admin != null) {
            model.addAttribute("admin", admin);
            List<Major> majors = majorService.getMajorsList();
            model.addAttribute("majors", majors);
            model.addAttribute("majors", majors);
            List<Integer> classesCountForEachMajor = new ArrayList<>();
            for (Major major : majors) {
                int count = majorService.getClassesCount(major);
                classesCountForEachMajor.add(count);
            }
            model.addAttribute("classesCountForEachMajor", classesCountForEachMajor);
            // ...其他代码...
            int adminCount = adminService.getAdminsList().size();
            int lecturerCount = lecturerService.getLecturersList().size();
            int studentCount = studentService.getStudentsList().size();
            model.addAttribute("adminCount", adminCount);
            model.addAttribute("lecturerCount", lecturerCount);
            model.addAttribute("studentCount", studentCount);
            int applicationCount = applicationService.getApplicationsCount();
            int offerCount = offerService.getOffersCount();
            int requirementCount = requirementService.getRequirementsCount();
            int positionCount = positionService.getPositionsCount();
            model.addAttribute("applicationCount", applicationCount);
            model.addAttribute("offerCount", offerCount);
            model.addAttribute("requirementCount", requirementCount);
            model.addAttribute("positionCount", positionCount);
        } else {
            return "login";
        }
        return "admin/index_admin"; // 这里返回的是视图名称
    }

    // 专业管理
    @GetMapping("/majors")
    public String showMajorsPage(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("user");
        if (admin != null) {
            model.addAttribute("admin", admin);
            List<Major> majors = majorService.getMajorsList();
            model.addAttribute("majors", majors);
            List<List<Integer>> classesCountForEachMajorAndStage = new ArrayList<>();
            for (Major major : majors) {
                List<Integer> classesCountForEachStage = new ArrayList<>();
                for (int stage = 1; stage <= 4; stage++) {
                    int count = majorService.getCoursesCountByStage(major, stage);
                    classesCountForEachStage.add(count);
                }
                classesCountForEachMajorAndStage.add(classesCountForEachStage);
            }
            model.addAttribute("classesCountForEachMajorAndStage", classesCountForEachMajorAndStage);
            return "admin/majors_admin";
        } else {
            return "login";
        }
    }


    // 添加专业
    @ResponseBody
    @PostMapping("/addMajor")
    public Result addMajor(@RequestBody Major major) {
        try {
            majorService.addMajor(major);
            return Result.success(major);
        } catch (Exception e) {
            return Result.error("add major failed: major already exists");
        }
    }


    // 某个专业的课程管理
    @GetMapping("/majors/{mid}")
    public String getMajorById(@PathVariable int mid, HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("user");
        Major major = majorService.getMajorById(mid);
        System.out.println(major);
        if (major != null) {
            model.addAttribute("admin", admin);
            model.addAttribute("major", major);
            List<Course> stage1 = majorService.getCoursesByStage(major, 1);
            List<Course> stage2 = majorService.getCoursesByStage(major, 2);
            List<Course> stage3 = majorService.getCoursesByStage(major, 3);
            List<Course> stage4 = majorService.getCoursesByStage(major, 4);
            for (Course course : stage1) {
                if (course.getLecturerId() != 0) {
                    course.setLecturer(lecturerService.getLecturerById(course.getLecturerId()));
                } else {
                    Lecturer temp = new Lecturer();
                    temp.setUsername("null");
                    course.setLecturer(temp);
                }
            }
            for (Course course : stage2) {
                if (course.getLecturerId() != 0) {
                    course.setLecturer(lecturerService.getLecturerById(course.getLecturerId()));
                } else {
                    Lecturer temp = new Lecturer();
                    temp.setUsername("null");
                    course.setLecturer(temp);
                }
            }
            for (Course course : stage3) {
                if (course.getLecturerId() != 0) {
                    course.setLecturer(lecturerService.getLecturerById(course.getLecturerId()));
                } else {
                    Lecturer temp = new Lecturer();
                    temp.setUsername("null");
                    course.setLecturer(temp);
                }
            }
            for (Course course : stage4) {
                if (course.getLecturerId() != 0) {
                    course.setLecturer(lecturerService.getLecturerById(course.getLecturerId()));
                } else {
                    Lecturer temp = new Lecturer();
                    temp.setUsername("null");
                    course.setLecturer(temp);
                }
            }
            model.addAttribute("stage1", stage1);
            model.addAttribute("stage2", stage2);
            model.addAttribute("stage3", stage3);
            model.addAttribute("stage4", stage4);
        } else {
            return "admin/majors_admin";
        }
        return "admin/major_admin";
    }

    // 添加课程
    @ResponseBody
    @PostMapping("/majors/{mid}/addCourse")
    public Result addCourse(@PathVariable int mid, @RequestBody Map<String, Course> courses) {
        try {
            Major major = majorService.getMajorById(mid);
            if (major != null) {
                Course lecture = courses.get("lecture");
                Course lab = courses.get("lab");
                int stage = lecture.getStage();
                Lecturer lecturer = lecturerService.getLecturerById(lecture.getLecturerId());
                System.out.println(lecture);
                System.out.println(lab);

                // 对于数量的判断
                if (!courseService.canAddCourse(lecture) || !moduleService.canAddModule(lecture.getCourseCode())) {
                    return Result.error("Course already exists");
                }
                // 对于时间冲突的判断，同专业同年级不能有同样的时间
                if (courseService.timeConflict(major, stage, lecture.getDay(), lecture.getTimeslot(), lecture.getTrimester(),
                        lab.getDay(), lab.getTimeslot())) {
                    return Result.error("Course time conflict");
                }
                if (moduleService.timeConflict(major, stage, lecture.getDay(), lecture.getTimeslot(), lecture.getTrimester(),
                        lab.getDay(), lab.getTimeslot())) {
                    return Result.error("Module time conflict");
                }
                if (lecturerService.timeConflict(lecturer, lecture.getDay(), lecture.getTimeslot(), lecture.getTrimester(),
                        lab.getDay(), lab.getTimeslot())) {
                    return Result.error("Lecturer time conflict");
                }

                majorService.addCourse(major, lecture);
                majorService.addCourse(major, lab);
                requirementService.addRequirementByCourse(lab);

                return Result.success(Arrays.asList(lecture, lab));
            } else {
                return Result.error("Major does not exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("Failed to add: " + e.getMessage());
        }
    }

    // 更新课程
    @ResponseBody
    @PutMapping("/majors/{mid}/{courseCode}")
    public Result editCourse(@PathVariable int mid, @PathVariable String courseCode, @RequestBody Map<String, Course> courses) {
        try {
            // 实现更新课程逻辑，根据 cid 更新课程信息
            // 如果需要，您可以添加逻辑来只更新特定字段
            Course lecture = courses.get("lecture");
            Course lab = courses.get("lab");
            int stage = lecture.getStage();
            Major major = majorService.getMajorById(mid);
            Lecturer lecturer = lecturerService.getLecturerById(lecture.getLecturerId());
            // 对于时间冲突的判断，同专业同年级不能有同样的时间
            if (courseService.timeConflictWithoutCode(major, stage, lecture.getDay(), lecture.getTimeslot(), lecture.getTrimester(),
                    lab.getDay(), lab.getTimeslot(), courseCode)) {
                return Result.error("Course time conflict");
            }
            if (moduleService.timeConflict(major, stage, lecture.getDay(), lecture.getTimeslot(), lecture.getTrimester(),
                    lab.getDay(), lab.getTimeslot())) {
                return Result.error("Module time conflict");
            }
            if (lecturerService.timeConflictWithoutCode(lecturer, lecture.getDay(), lecture.getTimeslot(), lecture.getTrimester(),
                    lab.getDay(), lab.getTimeslot(), courseCode)) {
                return Result.error("Lecturer time conflict");
            }
            courseService.updateCourse(courseCode, lecture);
            courseService.updateCourse(courseCode, lab);
            return Result.success("Update course successfully");
        } catch (Exception e) {
            return Result.error("Update failed: " + e.getMessage());
        }
    }

    // 删除课程
    @ResponseBody
    @DeleteMapping("/majors/{mid}/{courseCode}")
    public Result deleteCourse(@PathVariable int mid, @PathVariable String courseCode) {
        try {
            // 实现删除课程逻辑，根据 cid 删除课程
            Course labDelete = courseService.getCourseByCodeByLab(courseCode);
            requirementService.deleteRequirementByCourseId(labDelete.getCid());
            positionService.deletePositionByCourseId(labDelete.getCid());
            applicationService.deleteApplicationByCourseId(labDelete.getCid());
            offerService.deleteOfferByCourseId(labDelete.getCid());
            classModifyService.deleteClassModifyByCode(courseCode);
            courseService.deleteCourse(courseCode);
            System.out.println("Deleting course with code: " + courseCode); // 调试输出
            return Result.success("Delete course successfully");
        } catch (Exception e) {
            return Result.error("Delete failed: " + e.getMessage());
        }
    }


    // 公共课管理
    @GetMapping("/modules")
    public String showModulesPage(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("user");
        if (admin != null) {
            model.addAttribute("admin", admin);
            List<Major> majors = majorService.getMajorsList();
            List<Module> modules = moduleService.getModulesList();
            Map<String, List<Major>> majorsForEachModule = new HashMap<>();

            for (Module module : modules) {
                List<MajorModule> majorsForModule = moduleService.getMajorsForModule(module);
                List<Major> majorsForModuleList = new ArrayList<>();
                for (MajorModule majorModule : majorsForModule) {
                    majorsForModuleList.add(majorService.getMajorByCode(majorModule.getMajorCode()));
                }
                if (!majorsForModule.isEmpty()) {
                    majorsForEachModule.put(module.getModuleCode(), majorsForModuleList);
                }
                if (module.getLecturerId() != 0) {
                    Lecturer lecturer = lecturerService.getLecturerById(module.getLecturerId());
                    module.setLecturer(lecturer);
                } else {
                    Lecturer temp = new Lecturer();
                    temp.setUsername("null");
                    module.setLecturer(temp);
                }

            }
            model.addAttribute("majors", majors);
            model.addAttribute("modules", modules);
            model.addAttribute("majorsForEachModule", majorsForEachModule);
            return "admin/module_admin";
        } else {
            return "login";
        }
    }

    // 添加公共课
    @ResponseBody
    @PostMapping("/modules/addModule")
    public Result addModule(@RequestBody Map<String, Object> information) {
        try {
            Module lecture = new Module();
            Module lab = new Module();
            lecture.setModuleCode((String) information.get("moduleCode"));
            lab.setModuleCode((String) information.get("moduleCode"));
            lecture.setModuleName((String) information.get("moduleName"));
            lab.setModuleName((String) information.get("moduleName"));
            lecture.setTrimester(Trimester.valueOf((String) information.get("trimester")));
            lab.setTrimester(Trimester.valueOf((String) information.get("trimester")));
            lecture.setStage(Integer.parseInt((String) information.get("stage")));
            lab.setStage(Integer.parseInt((String) information.get("stage")));
            lecture.setType(Type.valueOf("Lecture"));
            lab.setType(Type.valueOf("Lab"));
            lecture.setDay(Day.valueOf((String) information.get("lectureDay")));
            lab.setDay(Day.valueOf((String) information.get("labDay")));
            lecture.setTimeslot(Timeslot.valueOf((String) information.get("lectureTime")));
            lab.setTimeslot(Timeslot.valueOf((String) information.get("labTime")));
            lecture.setLecturerId(Integer.parseInt((String) information.get("lecturerId")));
            lab.setLecturerId(Integer.parseInt((String) information.get("lecturerId")));
            Lecturer lecturer = lecturerService.getLecturerById(lecture.getLecturerId());
            @SuppressWarnings("unchecked")
            List<String> majorsList = (List<String>) information.get("majors");  // 使用 majorsList 来进行操作

            System.out.println("11111111111111" + lecture);

            if (!moduleService.canAddModule(lecture) || !courseService.canAddCourse(lecture.getModuleCode())) {
                return Result.error("Module already exists");
            }

            List<Major> majors = new ArrayList<>();
            for (String majorCode : majorsList) {
                majors.add(majorService.getMajorByCode(majorCode));
            }

            for (Major major : majors) {
                if (courseService.timeConflict(major, lecture.getStage(), lecture.getDay(), lecture.getTimeslot(), lecture.getTrimester(),
                        lab.getDay(), lab.getTimeslot())) {
                    return Result.error("Course time conflict");
                }
                if (moduleService.timeConflict(major, lecture.getStage(), lecture.getDay(), lecture.getTimeslot(), lecture.getTrimester(),
                        lab.getDay(), lab.getTimeslot())) {
                    return Result.error("Module time conflict");
                }
            }

            if (lecturerService.timeConflict(lecturer, lecture.getDay(), lecture.getTimeslot(), lecture.getTrimester(),
                    lab.getDay(), lab.getTimeslot())) {
                return Result.error("Lecturer time conflict");
            }

            moduleService.addModuleInModule(lab);
            for (String majorCode : majorsList) {
                moduleService.addModuleInMajorModule(lab, majorCode);
            }
            moduleService.addModuleInModule(lecture);
            requirementService.addRequirementByModule(lab);
            return Result.success(Arrays.asList(lecture, lab));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("Add failed: Module already exists");
        }
    }

    // 更新公共课
    @ResponseBody
    @PutMapping("/modules/updateModule")
    public Result updateModule(@RequestBody Map<String, Object> information) {
        try {
            Module lecture = new Module();
            Module lab = new Module();
            lecture.setModuleCode((String) information.get("moduleCode"));
            lab.setModuleCode((String) information.get("moduleCode"));
            lecture.setModuleName((String) information.get("moduleName"));
            lab.setModuleName((String) information.get("moduleName"));
            lecture.setTrimester(Trimester.valueOf((String) information.get("trimester")));
            lab.setTrimester(Trimester.valueOf((String) information.get("trimester")));
            lecture.setStage(Integer.parseInt((String) information.get("stage")));
            lab.setStage(Integer.parseInt((String) information.get("stage")));
            lecture.setType(Type.valueOf("Lecture"));
            lab.setType(Type.valueOf("Lab"));
            lecture.setDay(Day.valueOf((String) information.get("lectureDay")));
            lab.setDay(Day.valueOf((String) information.get("labDay")));
            lecture.setTimeslot(Timeslot.valueOf((String) information.get("lectureTime")));
            lab.setTimeslot(Timeslot.valueOf((String) information.get("labTime")));
            lecture.setLecturerId(Integer.parseInt((String) information.get("lecturerId")));
            lab.setLecturerId(Integer.parseInt((String) information.get("lecturerId")));
            int mid = Integer.parseInt((String) information.get("mid"));
            lab.setMid(mid);
            @SuppressWarnings("unchecked")
            List<String> majorsList = (List<String>) information.get("majors");  // 使用 majorsList 来进行操作
            Lecturer lecturer = lecturerService.getLecturerById(lecture.getLecturerId());

            System.out.println("11111111111111" + lecture);

            List<Major> majors = new ArrayList<>();
            for (String majorCode : majorsList) {
                majors.add(majorService.getMajorByCode(majorCode));
            }

            for (Major major : majors) {
                if (courseService.timeConflict(major, lecture.getStage(), lecture.getDay(), lecture.getTimeslot(), lecture.getTrimester(),
                        lab.getDay(), lab.getTimeslot())) {
                    return Result.error("Course time conflict");
                }
                if (moduleService.timeConflictWithoutCode(major, lecture.getStage(), lecture.getDay(), lecture.getTimeslot(), lecture.getTrimester(),
                        lab.getDay(), lab.getTimeslot(), lecture.getModuleCode())) {
                    return Result.error("Module time conflict");
                }
            }

            if (lecturerService.timeConflictWithoutCode(lecturer, lecture.getDay(), lecture.getTimeslot(), lecture.getTrimester(),
                    lab.getDay(), lab.getTimeslot(), lecture.getModuleCode())) {
                return Result.error("lecturer time conflict");
            }

            moduleService.updateModule(lab);
            moduleService.deleteModuleInMajorModule(mid);
            for (String majorCode : majorsList) {
                moduleService.addModuleInMajorModule(lab, majorCode);
            }
            moduleService.updateModule(lecture);
            return Result.success(Arrays.asList(lecture, lab));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("update failed: Module already exists");
        }
    }

    // 删除公共课
    @ResponseBody
    @DeleteMapping("/modules/deleteModule")
    public Result deleteModule(@RequestBody Map<String, Object> information) {
        try {
            String moduleCode = (String) information.get("moduleCode");
            Module labDelete = moduleService.getModuleByCodeByLab(moduleCode);
            requirementService.deleteRequirementByModuleId(labDelete.getMid());
            positionService.deletePositionByModuleId(labDelete.getMid());
            applicationService.deleteApplicationByModuleId(labDelete.getMid());
            offerService.deleteOfferByModuleId(labDelete.getMid());
            moduleService.deleteModuleInMajorModule((Integer) information.get("mid"));
            moduleService.deleteModuleInModule(information.get("moduleCode").toString());
            return Result.success("delete module successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("delete failed: module does not exist");
        }
    }


    @GetMapping("/accounts")
    public String accountsPage(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("user");
        if (admin != null) {
            model.addAttribute("admin", admin);
            List<Major> majors = majorService.getMajorsList();
            List<Admin> admins = adminService.getAdminsList();
            List<Lecturer> lecturers = lecturerService.getLecturersList();
            for (Lecturer lecturer : lecturers) {
                List<Course> labCourses = courseService.getCoursesLabListByUid(lecturer.getUid());
                List<Module> labModules = moduleService.getModulesLabListByUid(lecturer.getUid());
                lecturer.setLabCourses(labCourses);
                lecturer.setLabModules(labModules);
            }
            List<Student> students = studentService.getStudentsList();
            model.addAttribute("admins", admins);
            model.addAttribute("lecturers", lecturers);
            model.addAttribute("students", students);
            model.addAttribute("majors", majors);
            return "admin/account_admin";
        } else {
            return "login";
        }
    }

    @ResponseBody
    @PostMapping("/accounts/addAdmin")
    public Result addAdmin(@RequestBody Admin admin) {
        try {
            adminService.addAdmin(admin);
            return Result.success(admin);
        } catch (Exception e) {
            return Result.error("fail to add admin: admin already exists");
        }
    }

    @ResponseBody
    @PostMapping("/accounts/addLecturer")
    public Result addLecturer(@RequestBody Lecturer lecturer) {
        try {
            lecturerService.addLecturer(lecturer);
            return Result.success(lecturer);
        } catch (Exception e) {
            return Result.error("fail to add lecturer: lecturer already exists");
        }
    }

    @ResponseBody
    @PostMapping("/accounts/addStudent")
    public Result addStudent(@RequestBody Map<String, Object> information) {
        try {
            Student student = new Student();
            student.setUsername((String) information.get("username"));
            student.setPassword((String) information.get("password"));
            student.setEmail((String) information.get("email"));
            student.setStudentId((String) information.get("studentId"));
            student.setEducationStatus(EducationStatus.valueOf((String) information.get("educationStatus")));
            student.setStage(Integer.parseInt((String) information.get("stage")));
            student.setMajorCode((String) information.get("majorCode"));
            student.setTimeLimitation(Integer.parseInt((String) information.get("timeLimitation")));
            studentService.addStudent(student);
            return Result.success(student);
        } catch (Exception e) {
            return Result.error("fail to add student: student already exists");
        }
    }

    @ResponseBody
    @PutMapping("/accounts/updateAdmin")
    public Result updateAdmin(@RequestBody Admin admin) {
        try {
            adminService.updateAdmin(admin);
            return Result.success(admin);
        } catch (Exception e) {
            return Result.error("update admin failed: admin does not exist");
        }
    }

    @ResponseBody
    @PutMapping("/accounts/updateLecturer")
    public Result updateLecturer(@RequestBody Lecturer lecturer) {
        try {
            lecturerService.updateLecturer(lecturer);
            return Result.success(lecturer);
        } catch (Exception e) {
            return Result.error("update lecturer failed: lecturer does not exist");
        }
    }

    @ResponseBody
    @PutMapping("/accounts/updateStudent")
    public Result updateStudent(@RequestBody Student student) {
        try {
            studentService.updateStudent(student);
            return Result.success(student);
        } catch (Exception e) {
            return Result.error("update student failed: student does not exist");
        }
    }

    @ResponseBody
    @DeleteMapping("/accounts/deleteAdmin")
    public Result deleteAdmin(@RequestBody Map<String, Object> information) {
        try {
            adminService.deleteAdmin(Integer.parseInt((String) information.get("uid")));
            return Result.success("delete admin successfully");
        } catch (Exception e) {
            e.printStackTrace();

            return Result.error("delete admin failed: admin does not exist");
        }
    }

    @ResponseBody
    @DeleteMapping("/accounts/deleteLecturer")
    public Result deleteLecturer(@RequestBody Map<String, Object> information) {
        try {
            int uid = Integer.parseInt((String) information.get("uid"));
            courseService.updateCourseLecturer(uid);
            moduleService.updateModuleLecturer(uid);
            lecturerService.deleteLecturer(uid);
            return Result.success("delete lecturer successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("delete lecturer failed: lecturer does not exist");
        }
    }

    @ResponseBody
    @DeleteMapping("/accounts/deleteStudent")
    public Result deleteStudent(@RequestBody Map<String, Object> information) {
        try {
            studentService.deleteStudent(Integer.parseInt((String) information.get("uid")));
            return Result.success("delete student successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("delete student failed: student does not exist");
        }
    }

    @GetMapping("/requirements")
    public String showRequirementsPage(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("user");
        if (admin != null) {
            model.addAttribute("admin", admin);
            List<Requirements> requirementsList = requirementService.getRequirementsList();
            List<Requirements> showRequirementsList = new ArrayList<>();
            for (Requirements requirements: requirementsList) {
                if (requirements.getCid() != 0){
                    Course course = courseService.getCourseByCID(requirements.getCid());
                    requirements.setCode(course.getCourseCode());
                    showRequirementsList.add(requirements);
                } else {
                    Module module = moduleService.getModuleByMID(requirements.getMid());
                    requirements.setCode(module.getModuleCode());
                    showRequirementsList.add(requirements);
                }
            }
            model.addAttribute("requirementsList", showRequirementsList);
            return "admin/requirement";
        } else {
            return "login";
        }
    }
    @GetMapping("/offers")
    public String showOffersPage(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("user");
        if (admin != null) {
            model.addAttribute("admin", admin);
            List<Offers> offersList = offerService.getOffersList();
            List<Offers> showOffersList = new ArrayList<>();
            for (Offers offers:offersList) {
                if (offers.getCid() != 0 && offers.getUid() != 0){
                    Course course = courseService.getCourseByCID(offers.getCid());
                    offers.setCode(course.getCourseCode());
                    Student student = studentService.getStudentById(offers.getUid());
                    offers.setSid(student.getStudentId());
                    showOffersList.add(offers);
                } else if (offers.getCid() == 0 && offers.getUid() != 0){
                    Module module = moduleService.getModuleByMID(offers.getMid());
                    offers.setCode(module.getModuleCode());
                    Student student = studentService.getStudentById(offers.getUid());
                    offers.setSid(student.getStudentId());
                    showOffersList.add(offers);
                }
            }
            model.addAttribute("offersList", showOffersList);
            return "admin/offers";
        } else {
            return "login";
        }
    }

    @GetMapping("/application")
    public String adminApplication(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("user");
        if (admin != null) {
            model.addAttribute("admin", admin);
            List<Applications> applicationsList = applicationService.getApplicationsListByStatus(Status.Pending);
            List<Applications> showApplicationsList = new ArrayList<>();
            for (Applications applications:applicationsList){
                if (applications.getCid() != 0 && applications.getMid() == 0){
                    Course course = courseService.getCourseByCID(applications.getCid());
                    applications.setCode(course.getCourseCode());
                    Student student = studentService.getStudentById(applications.getUid());
                    applications.setStudent(student);
                    showApplicationsList.add(applications);
                } else if (applications.getCid() == 0 && applications.getMid() != 0){
                    Module module = moduleService.getModuleByMID(applications.getMid());
                    applications.setCode(module.getModuleCode());
                    Student student = studentService.getStudentById(applications.getUid());
                    applications.setStudent(student);
                    showApplicationsList.add(applications);
                }
            }
            model.addAttribute("applicationsList", showApplicationsList);
            return "admin/applications";
        } else {

            return "login";
        }

    }

    @ResponseBody
    @PutMapping("/applications/{aid}")
    public Result updateApplication(@PathVariable int aid, @RequestParam String status) {
        try {
            Status applicationStatus = Status.valueOf(status);
            applicationService.updateApplication(aid, applicationStatus);
            return Result.success("update application successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("update application failed: application does not exist");
        }
    }

    @GetMapping("/positions")
    public String adminPositionPage(HttpSession session, Model model){
        Admin admin = (Admin) session.getAttribute("user");
        if (admin != null) {
            model.addAttribute("admin", admin);
            List<Positions> positionsList = positionService.getPositionsList();
            List<Positions> showPositionsList = new ArrayList<>();
            for (Positions positions:positionsList){
                if (positions.getCid() != 0 && positions.getMid() == 0){
                    Course course = courseService.getCourseByCID(positions.getCid());
                    positions.setCode(course.getCourseCode());
                    Student student = studentService.getStudentById(positions.getUid());
                    positions.setStudent(student);
                    showPositionsList.add(positions);
                } else if (positions.getCid() == 0 && positions.getMid() != 0){
                    Module module = moduleService.getModuleByMID(positions.getMid());
                    positions.setCode(module.getModuleCode());
                    Student student = studentService.getStudentById(positions.getUid());
                    positions.setStudent(student);
                    showPositionsList.add(positions);
                }
            }
            model.addAttribute("positionsList", showPositionsList);
            return "admin/positions";
        } else {
            return "login";
        }
    }

    @GetMapping("classModify")
    public String showModify(HttpSession session, Model model){
        Admin admin = (Admin) session.getAttribute("user");
        if (admin != null) {
            model.addAttribute("admin", admin);
            List<ClassModify> classModifyList = classModifyService.getAllByPending();
            List<ClassModify> classModifyList1 = classModifyService.getAllWithoutPending();
            List<Course> courseList = new ArrayList<>();
            List<Course> courseList1 = new ArrayList<>();
            for (ClassModify classModify: classModifyList) {
                Course course = courseService.getCourseByCID(classModify.getCid());
                course.setPurpose(classModify.getPurpose());
                Student student = studentService.getStudentById(classModify.getStudentId());
                course.setStudent(student);
                courseList.add(course);
            }
            for (ClassModify classModify: classModifyList1) {
                Course course = courseService.getCourseByCID(classModify.getCid());
                course.setPurpose(classModify.getPurpose());
                course.setStatus(classModify.getStatus());
                Student student = studentService.getStudentById(classModify.getStudentId());
                course.setStudent(student);
                courseList1.add(course);
            }
            courseList.removeIf(course -> course.getType().name().equals("Lecture"));
            courseList1.removeIf(course -> course.getType().name().equals("Lecture"));
            model.addAttribute("courseList", courseList);
            model.addAttribute("courseList1", courseList1);
            return "admin/process_course_admin";
        } else {
            return "login";
        }
    }

    @ResponseBody
    @PutMapping("putClass/{uid}")
    public Result updateClassModify(@PathVariable int uid, @RequestParam String courseCode, @RequestParam String status){
        try {
            Status classModifyStatus = Status.valueOf(status);
            System.out.println(classModifyStatus);
            classModifyService.updateClassModify(uid, courseCode, classModifyStatus);
            return Result.success("update class modify successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("update class modify failed: class modify does not exist");
        }
    }

    @ResponseBody
    @GetMapping
    public List<Admin> getAdminsList() {
        return adminService.getAdminsList();
    }

    @ResponseBody
    @GetMapping("/{uid}")
    public Admin getAdminById(@PathVariable int uid) {
        return adminService.getAdminById(uid);
    }


    @ResponseBody
    @DeleteMapping("/{uid}")
    public void deleteAdmin(@PathVariable int uid) {
        adminService.deleteAdmin(uid);
    }
}