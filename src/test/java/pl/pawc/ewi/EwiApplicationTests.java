package pl.pawc.ewi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import pl.pawc.ewi.entity.TestEntity;
import pl.pawc.ewi.repository.TestEntityRepository;

@SpringBootTest
class EwiApplicationTests {

	@Autowired
	TestEntityRepository repository;

	@Test
	void contextLoads() {
	}

	@Test
	public void test(){
		TestEntity testEntity = new TestEntity();
		testEntity.setText("test");
		repository.save(testEntity);

	}

}
