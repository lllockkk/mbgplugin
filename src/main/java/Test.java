import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws Exception {
        File configFile = new File("src/main/resources/mbg.xml");
        System.out.println(configFile.getAbsolutePath());
        boolean overwrite = true;
        List<String> warnings = new ArrayList<String>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback cb = new DefaultShellCallback(overwrite);
        MyBatisGenerator generator = new MyBatisGenerator(config, cb, warnings);
        generator.generate(null);
    }
}
