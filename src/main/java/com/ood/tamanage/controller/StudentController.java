package com.ood.tamanage.controller;

import com.ood.tamanage.enums.Grade;
import com.ood.tamanage.enums.Purpose;
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

@Controller
@RequestMapping("/students")
public class StudentController {

    final static Trimester trimester = Trimester.Autumn;

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


    @GetMapping("/index_student")
    public String showStudentIndexPage(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("user");
        if (student != null) {
            model.addAttribute("student", student);
        } else {
            return "login";
        }
        return "student/index_student"; // 这里返回的是视图名称
    }

    @GetMapping("viewAPPOffer")
    public String showViewAPPOfferPage(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("user");
        if (student != null) {
            model.addAttribute("student", student);
            List<Offers> offersList = offerService.getOffersByUidPending(student.getUid());
            List<Offers> showOffers = new ArrayList<>();
            for (Offers offers : offersList) {
                if (offers.getCid() != 0) {
                    Course course = courseService.getCourseByCID(offers.getCid());
                    offers.setCode(course.getCourseCode());
                    showOffers.add(offers);
                } else {
                    Module module = moduleService.getModuleByMID(offers.getMid());
                    offers.setCode(module.getModuleCode());
                    showOffers.add(offers);
                }
            }
            model.addAttribute("offersList", showOffers);
            List<Applications> applicationsList = applicationService.getApplicationsListByUid(student.getUid());
            List<Applications> showApplications = new ArrayList<>();
            for (Applications applications : applicationsList) {
                if (applications.getCid() != 0) {
                    Course course = courseService.getCourseByCID(applications.getCid());
                    applications.setCode(course.getCourseCode());
                    showApplications.add(applications);
                } else {
                    Module module = moduleService.getModuleByMID(applications.getMid());
                    applications.setCode(module.getModuleCode());
                    showApplications.add(applications);
                }
            }
            model.addAttribute("applicationsList", showApplications);
        } else {
            return "login";
        }
        return "student/viewAppAndOffer"; // 这里返回的是视图名称
    }

    @ResponseBody
    @PutMapping("/offers/{uid}/{oid}")
    public Result updateOffer(@PathVariable int uid, @PathVariable int oid, @RequestParam String status) {
        try {
            Status offerStatus = Status.valueOf(status);
            offerService.updateOffer(oid, offerStatus);
            if (offerStatus.equals(Status.Rejected)) {
                return Result.success("offer rejected");
            }
            Offers offer = offerService.getOfferByOid(oid);
            if (offer.getCid() != 0) {
                positionService.addPositionByCid(uid, offer.getCid());
            } else {
                positionService.addPositionByMid(uid, offer.getMid());
            }
            return Result.success("offer accepted");
        } catch (IllegalArgumentException e) {
            return Result.error("Invalid status value");
        } catch (Exception e) {
            return Result.error("error");
        }
    }


    @GetMapping("/application")
    public String showApplicationPage(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("user");
        if (student != null) {
            model.addAttribute("student", student);
            List<Course> labCourses = new ArrayList<>();
            List<Module> labModules = new ArrayList<>();
            for (int i = 1; i <= student.getStage(); i++) {
                List<Course> stageCourses = courseService.getLabCoursesByMajorByStage(student.getMajorCode(), i);
                labCourses.addAll(stageCourses);
            }

            List<ClassModify> courseModify = classModifyService.getAllByStudent(student.getUid());
            List<Course> appendCourse = new ArrayList<>();
            List<Course> removeCourse = new ArrayList<>();
            for (ClassModify classModify : courseModify) {
                if (classModify.getPurpose().name().equals("Append")) {
                    Course course = courseService.getCourseByCID(classModify.getCid());
                    course.setPurpose(classModify.getPurpose());
                    course.setStatus(classModify.getStatus());
                    appendCourse.add(course);
                } else {
                    Course course = courseService.getCourseByCID(classModify.getCid());
                    course.setPurpose(classModify.getPurpose());
                    course.setStatus(classModify.getStatus());
                    removeCourse.add(course);
                }
            }
            appendCourse.removeIf(course -> course.getType().name().equals("Lecture"));
            for (Course course : appendCourse) {
                if (course.getStatus().name().equals("Accepted")) {
                    labCourses.add(course);
                }
            }
            removeCourse.removeIf(course -> course.getType().name().equals("Lecture"));
            for (Course course : removeCourse) {
                if (course.getStatus().name().equals("Accepted")) {
                    for (Course c:labCourses) {
                        if (c.getCid() == course.getCid()) {
                            labCourses.remove(c);
                            break;
                        }
                    }
                }
            }

            model.addAttribute("courses", labCourses);
            for (int i = 1; i <= student.getStage(); i++) {
                List<Module> stageModules = moduleService.getLabModulesByStage(i);
                for (Module module : stageModules) {
                    List<MajorModule> majorModules = moduleService.getMajorsForModule(module);
                    List<String> majorCodes = new ArrayList<>();
                    for (MajorModule majorModule : majorModules) {
                        majorCodes.add(majorModule.getMajorCode());
                    }
                    if (majorCodes.contains(student.getMajorCode())) {
                        System.out.println(module.getModuleName());
                        labModules.add(module);
                    }
                }
            }
            model.addAttribute("modules", labModules);
        } else {
            return "login";
        }
        return "student/application_student"; // 这里返回的是视图名称
    }

    @ResponseBody
    @PostMapping("/sendApplication")
    public Result sendApp(@RequestBody Map<String, Object> information) {
        try {
            int uid = Integer.parseInt((String) information.get("studentId"));
            Student student = studentService.getStudentById(uid);
            String type = (String) information.get("type");
            System.out.println(type);
            int classId = Integer.parseInt((String) information.get("classId"));
            Grade studentGrade = Grade.valueOf((String) information.get("studentGrade"));
            Grade studentProject = Grade.valueOf((String) information.get("studentProject"));
            String description = (String) information.get("description");
            String majorCode = (String) information.get("majorCode");
            int stage = Integer.parseInt((String) information.get("stage"));

            Major major = majorService.getMajorByCode(majorCode);

            if (type.equals("course")) {
                Course course = courseService.getCourseByCID(classId);
                if (offerService.hasStuedentOfferByCID(classId, uid)) {
                    return Result.error("apply failed, already offered");
                }
                if (studentService.applyForCourse(uid, classId)) {
                    return Result.error("apply failed, already applied");
                }
                if (studentService.checkTimeConflict(uid, course.getDay(), course.getTimeslot(), trimester, course.getDay(), course.getTimeslot())) {
                    return Result.error("apply failed, time conflict");
                }
                if (moduleService.timeConflict(major, stage, course.getDay(), course.getTimeslot(), trimester, course.getDay(), course.getTimeslot())) {
                    return Result.error("apply failed, time conflict");
                }
                Requirements requirement = requirementService.getRequirementsByCID(course.getCid());
                if (requirement.getQuantity() == 0) {

                    applicationService.addApplication(uid, classId, Status.Pending, studentGrade, studentProject, description);
                    return Result.success("apply success");
                }
                if (requirement.getStageRequirement() > stage || requirement.isUndergrad() || requirement.isMaster()) {
                    return Result.error("apply failed, grade not match");
                }
                if (!positionService.canAddPositionByCid(classId, requirement.getQuantity())) {
                    return Result.error("apply failed, position full");
                }

                if (studentService.workHoursExceed(uid, student.getTimeLimitation(), requirement.getWorkHours())) {
                    return Result.error("apply failed, work hours exceed");
                }
                applicationService.addApplication(uid, classId, Status.Pending, studentGrade, studentProject, description);
            } else {
                Module module = moduleService.getModuleByMID(classId);
                if (offerService.hasStuedentOfferByMID(classId, uid)) {
                    return Result.error("apply failed, already offered");
                }
                if (studentService.applyForModule(uid, classId)) {
                    System.out.println(module);
                    return Result.error("apply failed, already applied");
                }
                System.out.println(module);
                if (courseService.timeConflict(major, stage, module.getDay(), module.getTimeslot(), trimester, module.getDay(), module.getTimeslot())) {
                    return Result.error("apply failed, time conflict");
                }
                if (moduleService.timeConflict(major, stage, module.getDay(), module.getTimeslot(), trimester, module.getDay(), module.getTimeslot())) {
                    return Result.error("apply failed, time conflict");
                }
                Requirements requirement = requirementService.getRequirementsByMID(module.getMid());
                if (requirement.getQuantity() == 0) {
                    System.out.println(module);
                    applicationService.addApplicationByM(uid, classId, Status.Pending, studentGrade, studentProject, description);
                    return Result.success("apply success");
                }
                if (requirement.getStageRequirement() > stage || requirement.isUndergrad() || requirement.isMaster()) {
                    System.out.println(module);

                    return Result.error("apply failed, grade not match");
                }
                if (!positionService.canAddPositionByMid(classId, requirement.getQuantity())) {
                    System.out.println(module);

                    return Result.error("apply failed, position full");
                }

                if (studentService.workHoursExceed(uid, student.getTimeLimitation(), requirement.getWorkHours())) {
                    System.out.println(module);

                    return Result.error("apply failed, work hours exceed");
                }
                applicationService.addApplicationByM(uid, classId, Status.Pending, studentGrade, studentProject, description);
            }

            return Result.success("apply success");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("apply failed");
        }
    }

    @GetMapping("/positions")
    public String showStudentPositions(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("user");
        if (student != null) {
            model.addAttribute("student", student);
            List<Positions> positionsList = studentService.getPositionsListByUid(student.getUid());
            List<Positions> showPositions = new ArrayList<>();
            for (Positions positions : positionsList) {
                if (positions.getCid() != 0) {
                    Course course = courseService.getCourseByCID(positions.getCid());
                    positions.setCode(course.getCourseCode());
                    showPositions.add(positions);
                } else {
                    Module module = moduleService.getModuleByMID(positions.getMid());
                    positions.setCode(module.getModuleCode());
                    showPositions.add(positions);
                }
            }
            model.addAttribute("positionsList", showPositions);
        } else {
            return "login";
        }
        return "student/pers_positions"; // 这里返回的是视图名称
    }

    @GetMapping("/timetable")
    public String showStudentTimetable(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("user");
        if (student != null) {
            model.addAttribute("student", student);
            List<Course> courses = courseService.getCourseByMajorByStageByTrimester(student.getMajorCode(), student.getStage(), trimester);
            List<Module> modules = moduleService.getModulesByMajorByStageByTrimester(student.getMajorCode(), student.getStage(), trimester);
            model.addAttribute("modules", modules);

            List<ClassModify> courseModify = classModifyService.getAllByStudent(student.getUid());
            List<Course> appendCourse = new ArrayList<>();
            List<Course> removeCourse = new ArrayList<>();
            for (ClassModify classModify : courseModify) {
                if (classModify.getPurpose().name().equals("Append")) {
                    Course course = courseService.getCourseByCID(classModify.getCid());
                    course.setPurpose(classModify.getPurpose());
                    course.setStatus(classModify.getStatus());
                    appendCourse.add(course);
                } else {
                    Course course = courseService.getCourseByCID(classModify.getCid());
                    course.setPurpose(classModify.getPurpose());
                    course.setStatus(classModify.getStatus());
                    removeCourse.add(course);
                }
            }
            for (Course course : appendCourse) {
                if (course.getStatus().name().equals("Accepted")) {
                    courses.add(course);
                }
            }
            for (Course course : removeCourse) {
                if (course.getStatus().name().equals("Accepted")) {
                    for (Course c:courses) {
                        if (c.getCid() == course.getCid()) {
                            courses.remove(c);
                            break;
                        }
                    }
                }
            }

            model.addAttribute("courses", courses);
            List<Positions> positionsList = studentService.getPositionsListByUid(student.getUid());
            List<Positions> showPositions = new ArrayList<>();
            List<Course> positionCourses = new ArrayList<>();
            List<Module> positionModules = new ArrayList<>();
            for (Positions positions : positionsList) {
                if (positions.getCid() != 0) {
                    Course course = courseService.getCourseByCID(positions.getCid());
                    if (course.getTrimester().name().equals(trimester.name())) {
                        positionCourses.add(course);
                    }

                } else {
                    Module module = moduleService.getModuleByMID(positions.getMid());
                    if (module.getTrimester().name().equals(trimester.name())) {
                        positionModules.add(module);
                    }
                }
            }
            model.addAttribute("pcourses", positionCourses);
            model.addAttribute("pmodules", positionModules);
        } else {
            return "login";
        }
        return "student/timetable_s"; // 这里返回的是视图名称
    }

    @GetMapping("courses")
    public String viewClass(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("user");
        if (student != null) {
            model.addAttribute("student", student);
            List<Course> courses = courseService.getCourseByMajorByStageByTrimester(student.getMajorCode(), student.getStage(), trimester);
            List<ClassModify> courseModify = classModifyService.getAllByStudent(student.getUid());
            List<Course> appendCourse = new ArrayList<>();
            List<Course> removeCourse = new ArrayList<>();
            List<Course> resultCourses = new ArrayList<>();
            for (ClassModify classModify : courseModify) {
                if (classModify.getPurpose().name().equals("Append")) {
                    Course course = courseService.getCourseByCID(classModify.getCid());
                    course.setPurpose(classModify.getPurpose());
                    course.setStatus(classModify.getStatus());
                    appendCourse.add(course);
                } else {
                    Course course = courseService.getCourseByCID(classModify.getCid());
                    course.setPurpose(classModify.getPurpose());
                    course.setStatus(classModify.getStatus());
                    removeCourse.add(course);
                }
            }
            resultCourses.addAll(appendCourse);
            resultCourses.addAll(removeCourse);
            for (Course course : appendCourse) {
                if (course.getStatus().name().equals("Accepted")) {
                    courses.add(course);
                }
            }
            for (Course course : removeCourse) {
                if (course.getStatus().name().equals("Accepted")) {
                    for (Course c:courses) {
                        if (c.getCid() == course.getCid()) {
                            courses.remove(c);
                            break;
                        }
                    }
                } else {
                    // 如果课程的状态不是 'Accepted'，则在 'courses' 列表中找到对应的课程并更新其状态
                    for (Course c : courses) {
                        if (c.getCid() == (course.getCid())) {
                            c.setPurpose(course.getPurpose());
                            c.setStatus(course.getStatus());
                            break;
                        }
                    }
                }
            }
            resultCourses.removeIf(course -> course.getType().name().equals("Lecture"));
            courses.removeIf(course -> course.getType().name().equals("Lecture"));
            model.addAttribute("courses", courses);
            model.addAttribute("resultCourses", resultCourses);
        } else {
            return "login";
        }
        return "student/courses_student"; // 这里返回的是视图名称
    }

    @ResponseBody
    @PostMapping("rejectCourse/{uid}/{cid}")
    public Result rejectOneCourse(@PathVariable int uid, @PathVariable int cid) {
        try {
            Course course = courseService.getCourseByCID(cid);
            List<Course> courses = courseService.getCourseByCode(course.getCourseCode());
            for (Course course1:courses) {
                classModifyService.addCourseModify(Purpose.Remove, Status.Pending, course1.getCid(),uid);
            }
            return Result.success("reject message send success");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("error");
        }
    }

    @ResponseBody
    @PostMapping("addCourse/{uid}")
    public Result addCourse(@PathVariable int uid, @RequestParam String courseCode) {
        try {
            List<Course> courses = courseService.getCourseByCode(courseCode);
            System.out.println(courses);
            if (courses.isEmpty()){
                return Result.error("find no course");
            }
            Course course1 = courses.get(0);
            Course course2 = courses.get(1);
            if (studentService.checkTimeConflict(uid, course1.getDay(), course1.getTimeslot(), trimester, course2.getDay(), course2.getTimeslot())) {
                return Result.error("apply failed, time conflict");
            }
            if (classModifyService.hasModify(uid, course1.getCid()) || classModifyService.hasModify(uid, course2.getCid())){
                return Result.error("already applied");
            }
            classModifyService.addCourseModify(Purpose.Append, Status.Pending, course1.getCid(),uid);
            classModifyService.addCourseModify(Purpose.Append, Status.Pending, course2.getCid(),uid);
            return Result.success("add message send success");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("error");
        }
    }

    /**
     * 全查询
     *
     * @return
     */
    @ResponseBody
    @GetMapping
    public List<Student> getStudentsList() {
        return studentService.getStudentsList();
    }

    /**
     * 根据uid查询
     *
     * @param uid
     * @return
     */
    @ResponseBody
    @GetMapping("/{uid}")
    public Student getStudentById(@PathVariable int uid) {
        return studentService.getStudentById(uid);
    }

    /**
     * 根据学号查询
     *
     * @param studentId
     * @return
     */
    @ResponseBody
    @GetMapping("/studentId/{studentId}")
    public Student getStudentByStudentId(@PathVariable String studentId) {
        return studentService.getStudentByStudentId(studentId);
    }

    /**
     * 添加学生信息
     *
     * @param student
     */
    @ResponseBody
    @PostMapping
    public void addStudent(Student student) {
        studentService.addStudent(student);
    }

    /**
     * 更新学生信息
     *
     * @param student
     */
    @ResponseBody
    @PutMapping
    public void updateStudent(Student student) {
        studentService.updateStudent(student);
    }

    /**
     * 删除学生信息
     *
     * @param uid
     */
    @ResponseBody
    @DeleteMapping("/{uid}")
    public void deleteStudent(@PathVariable int uid) {
        studentService.deleteStudent(uid);
    }


}
