package pl.edu.pw.elka.paprykaisalami.geeruh.support;

import org.json.JSONObject;

public class ProjectDataset {

    public static final String FIRST_PROJECT_CODE = "FST";
    public static final String FIRST_PROJECT_NAME = "First project";
    public static final String FIRST_PROJECT_DESCRIPTION = "Important project";
    public static final JSONObject FIRST_PROJECT = new JSONObject()
            .put("name", FIRST_PROJECT_NAME)
            .put("description", FIRST_PROJECT_DESCRIPTION);
    public static final String FIRST_PROJECT_STRING = FIRST_PROJECT.toString();

    public static final String SECOND_PROJECT_CODE = "SND";
    public static final String SECOND_PROJECT_NAME = "Second project";
    public static final JSONObject SECOND_PROJECT_NO_DESCRIPTION = new JSONObject()
            .put("name", SECOND_PROJECT_NAME);
    public static final String SECOND_PROJECT_NO_DESCRIPTION_STRING = FIRST_PROJECT.toString();
}
