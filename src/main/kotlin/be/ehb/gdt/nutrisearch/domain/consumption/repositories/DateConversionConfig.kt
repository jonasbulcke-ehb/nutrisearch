package be.ehb.gdt.nutrisearch.domain.consumption.repositories

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@Configuration
@SuppressWarnings("kotlin:S6516")
class DateConversionConfig {
    @Bean
    fun mongoCustomConversions() = MongoCustomConversions(
        listOf(
            object : Converter<LocalDate, String> {
                override fun convert(it: LocalDate) = it.toString()
            },
            object : Converter<String, LocalDate> {
                override fun convert(it: String) = LocalDate.parse(it)
            },
            object : Converter<LocalDate, Long> {
                override fun convert(source: LocalDate) = source.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
            },
            object : Converter<Long, LocalDate> {
                override fun convert(source: Long) =
                    Instant.ofEpochSecond(source).atOffset(ZoneOffset.UTC).toLocalDate()
            }
        )
    )
}