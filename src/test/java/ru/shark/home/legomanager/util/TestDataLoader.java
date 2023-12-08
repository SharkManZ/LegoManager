package ru.shark.home.legomanager.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.shark.home.legomanager.dao.entity.*;
import ru.shark.home.legomanager.dao.entity.load.PartLoadComparisonEntity;
import ru.shark.home.legomanager.dao.entity.load.PartLoadSkipEntity;
import ru.shark.home.legomanager.dao.repository.*;
import ru.shark.home.legomanager.util.dto.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class TestDataLoader {
    private static final ObjectMapper mapper = new JsonMapper();
    private static final List<String> cleanUpLst = Arrays.asList(
            "LEGO_PART_LOAD_COMPARISON",
            "LEGO_PART_LOAD_SKIP",
            "LEGO_USER_PARTS",
            "LEGO_USER_SETS",
            "LEGO_USERS",
            "LEGO_SET_PART",
            "LEGO_PART_COLOR_NUMBER",
            "LEGO_PART_COLOR",
            "LEGO_PART_NUMBER",
            "LEGO_PART",
            "LEGO_PART_CATEGORY",
            "LEGO_COLOR",
            "LEGO_SET",
            "LEGO_SERIES"
    );

    @Autowired
    private ru.shark.home.legomanager.util.TestEntityFinder entityFinder;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private SetRepository setRepository;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private PartCategoryRepository partCategoryRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private PartColorRepository partColorRepository;

    @Autowired
    private SetPartRepository setPartRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserSetsRepository userSetsRepository;

    @Autowired
    private UserPartsRepository userPartsRepository;

    @Autowired
    private PartNumberRepository partNumberRepository;

    @Autowired
    private PartColorNumberRepository partColorNumberRepository;

    @Autowired
    private PartLoadSkipRepository partLoadSkipRepository;

    @Autowired
    private PartLoadComparisonRepository partLoadComparisonRepository;

    @PersistenceContext
    private EntityManager em;

    /**
     * Загружает файлы с данными для тестов.
     *
     * @param files массив
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void loadSeries(String... files) {
        for (String file : files) {
            try {
                File fl = new File(this.getClass().getResource("/testData/" + file).toURI());
                List<SeriesEntity> list = mapper.readValue(fl, new TypeReference<List<SeriesEntity>>() {
                });
                list.forEach(entity -> {
                    seriesRepository.save(entity);
                });
            } catch (URISyntaxException e) {
                System.out.println("missing file: " + "/json/" + file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Загружает файлы с данными для тестов.
     *
     * @param files массив
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void loadSets(String... files) {
        for (String file : files) {
            try {
                File fl = new File(this.getClass().getResource("/testData/" + file).toURI());
                List<SetTestDto> list = mapper.readValue(fl, new TypeReference<List<SetTestDto>>() {
                });
                list.forEach(dto -> {
                    SetEntity entity = mapTestSetToEntity(dto);
                    entity.setSeries(new SeriesEntity());
                    entity.getSeries().setId(entityFinder.findSeriesId(dto.getSeries()));
                    setRepository.save(entity);

                    if (!isEmpty(dto.getParts())) {
                        loadSetPartColors(dto.getParts(), entity);
                    }
                });
            } catch (URISyntaxException e) {
                System.out.println("missing file: " + "/json/" + file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadSetPartColors(List<SetPartTestDto> list, SetEntity setEntity) {
        for (SetPartTestDto dto : list) {
            SetPartEntity entity = new SetPartEntity();
            entity.setSet(setEntity);
            entity.setPartColor(entityFinder.findPartColor(dto.getNumber()));
            entity.setCount(dto.getCount());

            setPartRepository.save(entity);
        }
    }

    /**
     * Загружает файлы с данными для тестов.
     *
     * @param files массив
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void loadColors(String... files) {
        for (String file : files) {
            try {
                File fl = new File(this.getClass().getResource("/testData/" + file).toURI());
                List<ColorEntity> list = mapper.readValue(fl, new TypeReference<List<ColorEntity>>() {
                });
                list.forEach(entity -> {
                    colorRepository.save(entity);
                });
            } catch (URISyntaxException e) {
                System.out.println("missing file: " + "/json/" + file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Загружает файлы с данными для тестов.
     *
     * @param files массив
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void loadPartCategories(String... files) {
        for (String file : files) {
            try {
                File fl = new File(this.getClass().getResource("/testData/" + file).toURI());
                List<PartCategoryEntity> list = mapper.readValue(fl, new TypeReference<List<PartCategoryEntity>>() {
                });
                list.forEach(entity -> {
                    partCategoryRepository.save(entity);
                });
            } catch (URISyntaxException e) {
                System.out.println("missing file: " + "/json/" + file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Загружает файлы с данными для тестов.
     *
     * @param files массив
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void loadParts(String... files) {
        for (String file : files) {
            try {
                File fl = new File(this.getClass().getResource("/testData/" + file).toURI());
                List<PartTestDto> list = mapper.readValue(fl, new TypeReference<List<PartTestDto>>() {
                });
                list.forEach(dto -> {
                    PartEntity entity = mapTestPartToEntity(dto);
                    entity.setCategory(new PartCategoryEntity());
                    entity.getCategory().setId(entityFinder.findPartCategoryId(dto.getCategory()));
                    entity = partRepository.save(entity);
                    if (!isEmpty(dto.getNumbers())) {
                        for (NumberTestDto numberDto : dto.getNumbers()) {
                            saveTestPartNumberToEntity(entity, numberDto);
                        }
                    }
                    if (!isEmpty(dto.getColors())) {
                        for (PartColorTestDto color : dto.getColors()) {
                            PartColorEntity partColorEntity = new PartColorEntity();
                            partColorEntity.setColor(entityFinder.findColor(color.getColor()));
                            partColorEntity.setPart(entity);
                            partColorEntity = partColorRepository.save(partColorEntity);

                            if (!isEmpty(color.getNumbers())) {
                                for (NumberTestDto numberDto : color.getNumbers()) {
                                    saveTestPartColorNumberToEntity(partColorEntity, numberDto);
                                }
                            }
                        }
                    }

                });
            } catch (URISyntaxException e) {
                System.out.println("missing file: " + "/json/" + file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveTestPartNumberToEntity(PartEntity partEntity, NumberTestDto dto) {
        PartNumberEntity entity = new PartNumberEntity();
        entity.setPart(partEntity);
        entity.setMain(dto.isMain());
        entity.setNumber(dto.getNumber());

        partNumberRepository.save(entity);
    }

    private void saveTestPartColorNumberToEntity(PartColorEntity partEntity, NumberTestDto dto) {
        PartColorNumberEntity entity = new PartColorNumberEntity();
        entity.setPartColor(partEntity);
        entity.setMain(dto.isMain());
        entity.setNumber(dto.getNumber());

        partColorNumberRepository.save(entity);
    }

    private SetEntity mapTestSetToEntity(SetTestDto dto) {
        SetEntity entity = new SetEntity();
        entity.setName(dto.getName());
        entity.setNumber(dto.getNumber());
        entity.setYear(dto.getYear());

        return entity;
    }

    private PartEntity mapTestPartToEntity(PartTestDto dto) {
        PartEntity entity = new PartEntity();
        entity.setName(dto.getName());
        return entity;
    }

    /**
     * Загружает файлы с данными для тестов.
     *
     * @param files массив
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void loadUsers(String... files) {
        for (String file : files) {
            try {
                File fl = new File(this.getClass().getResource("/testData/" + file).toURI());
                List<UserTestDto> list = mapper.readValue(fl, new TypeReference<List<UserTestDto>>() {
                });
                for (UserTestDto userDto : list) {
                    UserEntity userEntity = mapTestUserToEntity(userDto);
                    userEntity = usersRepository.save(userEntity);
                    if (isEmpty(userDto.getSets())) {
                        continue;
                    }

                    for (UserSetTestDto setTestDto : userDto.getSets()) {
                        SetEntity setEntity = setRepository.findSetByNumber(setTestDto.getNumber());
                        UserSetEntity userSetEntity = new UserSetEntity();
                        userSetEntity.setUser(userEntity);
                        userSetEntity.setSet(setEntity);
                        userSetEntity.setCount(setTestDto.getCount());
                        userSetsRepository.save(userSetEntity);
                    }

                    if (!isEmpty(userDto.getParts())) {
                        for (UserPartTestDto part : userDto.getParts()) {
                            UserPartEntity partEntity = new UserPartEntity();
                            partEntity.setUser(userEntity);
                            partEntity.setPartColor(entityFinder.findPartColor(part.getNumber()));
                            partEntity.setCount(part.getCount());
                            userPartsRepository.save(partEntity);
                        }
                    }

                }
            } catch (URISyntaxException e) {
                System.out.println("missing file: " + "/json/" + file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Загружает файлы с данными для тестов.
     *
     * @param files массив
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void loadPartSkip(String... files) {
        for (String file : files) {
            try {
                File fl = new File(this.getClass().getResource("/testData/" + file).toURI());
                List<String> list = mapper.readValue(fl, new TypeReference<List<String>>() {
                });
                for (String ptn : list) {
                    PartLoadSkipEntity partLoadSkipEntity = new PartLoadSkipEntity();
                    partLoadSkipEntity.setPattern(ptn);
                    partLoadSkipRepository.save(partLoadSkipEntity);
                }
            } catch (URISyntaxException e) {
                System.out.println("missing file: " + "/json/" + file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void loadPartLoadComparison(String... files) {
        for (String file : files) {
            try {
                File fl = new File(this.getClass().getResource("/testData/" + file).toURI());
                List<PartLoadComparisonTestDto> list = mapper.readValue(fl, new TypeReference<>() {
                });

                for (PartLoadComparisonTestDto dto : list) {
                    PartLoadComparisonEntity entity = new PartLoadComparisonEntity();
                    entity.setLoadNumber(dto.getNumber());
                    entity.setPartName(dto.getName());
                    entity.setPartColor(partColorRepository.getPartColorByNumberPartNumber(dto.getPartColorNumber(), dto.getPartNumber()).iterator().next());
                    partLoadComparisonRepository.save(entity);
                }

            } catch (URISyntaxException e) {
                System.out.println("missing file: " + "/json/" + file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private UserEntity mapTestUserToEntity(UserTestDto dto) {
        UserEntity entity = new UserEntity();
        entity.setName(dto.getName());

        return entity;
    }

    /**
     * Удаляет все данные из определенного списка таблиц
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cleanUp() {
        cleanUpLst.forEach(item -> {
            em.createNativeQuery(" delete from " + item).executeUpdate();
        });
    }
}
