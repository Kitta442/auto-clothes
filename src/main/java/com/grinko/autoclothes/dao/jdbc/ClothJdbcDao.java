package com.grinko.autoclothes.dao.jdbc;

import com.grinko.autoclothes.dao.ClothDao;
import com.grinko.autoclothes.dao.model.ClothTuple;
import com.grinko.autoclothes.util.DbUtil;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.grinko.autoclothes.util.CollectionUtil.mapOf;
import static com.grinko.autoclothes.util.ColumnMapper.columnMapper;

@Slf4j
@Timed
@Component
@RequiredArgsConstructor
public class ClothJdbcDao implements ClothDao {

    private final Function<String, String> sqls = DbUtil.sqls("auto-clothes");

    private final NamedParameterJdbcTemplate template;

    private static void mapTuple(final ResultSet rs,
                                 final Consumer<ClothTuple> consumer) {
        var b = ClothTuple.builder();
        columnMapper(rs)
            .asString("id", b::id)
            .asString("name", b::name)
            .asString("vendorCode", b::vendorCode);
        consumer.accept(b.build());
    }

    @Override
    public void fetchAllCloths(final Consumer<ClothTuple> consumer) {
        template.query(
            sqls.apply("fetch-all-cloths.sql"),
            rs -> {
                mapTuple(rs, consumer);
            }
        );
    }

    @Override
    public void fetchClothById(final String id,
                               final Consumer<ClothTuple> consumer) {
        template.query(
            sqls.apply("fetch-cloth-by-id.sql"),
            mapOf("id", id),
            rs -> {
                mapTuple(rs, consumer);
            }
        );

    }
}
