import javax2.lang.macro.ExampleMacro;
import java.util.Date;

public class Example {

    @GetSet @NonNull
    private String lastName;
    
    @GetSet @NonNull
    private String firstName;
    
    @GetSet
    private String organization;

    @Get @NonNull
    private Date dateAdded;

}


@interface GetSet {}
@interface Get {}
@interface Set {}
@interface NonNull {}

