package com.globant.automation.trainings.servicetesting.tests.spring.repositories;

import com.globant.automation.trainings.servicetesting.models.Label;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Juan Krzemien
 */
public interface LabelsRepository extends PagingAndSortingRepository<Label, Long> {
}
