package test.soprasteria.modelling.service.user;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;
import com.soprasteria.modelling.api.context.WebServiceAuthManager;
import com.soprasteria.modelling.service.base.UserService;
import com.soprasteria.modelling.service.base.entity.UserDomain;

public class UserServiceTest {

    //	@Test
    public void testAuth() throws Exception {
        WebServiceAuthManager service = new WebServiceAuthManager();
//		UserDomain user = service.authenUser("812138;p:bz03");

        UserDomain user = service.authenUser("test", "test123");
//		UserDomain user = service.authenUser("cc6681fe-f4b4-446c-b67c-57aac4dc10cd");
        System.out.println(new Gson().toJson(user));
    }

    //@Test
    public void testlogin() throws Exception {
        String username = "admin";
        String password = "test123";
        UserService service = new UserService();
        service.authUser(username, password);
    }

    @Test
    public void testAllUser() throws Exception {

        UserService service = new UserService();
        List<UserDomain> result = service.queryAllUser(new HashMap<String, String>());
        System.out.println(result.size());
    }

    @Test
    public void testQueryAllUserByProject() throws Exception {
        UserService service = new UserService();
        List<UserDomain> result = service.queryAllUserByProjectId("shsj01","admin");
        for (UserDomain user:result){
            System.out.println(user.getProjects().size());
        }
        System.out.println(result.size());
    }

    //	@Test
    public void testCheckVerificationCode() throws Exception {
        UserService service = new UserService();
        Boolean effective = service.checkVerificationCode("a4b7db9691a94c77896cadfdb3a9051b");
        Boolean effective2 = service.checkUsernameByVerificationCode("asdfg", "a4b7db9691a94c77896cadfdb3a9051b");
        service.resetPassword("asdfg", "a4b7db9691a94c77896cadfdb3a9051b", "12345678");
        System.out.println(effective + "" + effective2);
    }
}
