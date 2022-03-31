package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassRoomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassRoom.class);
        ClassRoom classRoom1 = new ClassRoom();
        classRoom1.setId(1L);
        ClassRoom classRoom2 = new ClassRoom();
        classRoom2.setId(classRoom1.getId());
        assertThat(classRoom1).isEqualTo(classRoom2);
        classRoom2.setId(2L);
        assertThat(classRoom1).isNotEqualTo(classRoom2);
        classRoom1.setId(null);
        assertThat(classRoom1).isNotEqualTo(classRoom2);
    }
}
