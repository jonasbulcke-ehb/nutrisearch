package be.ehb.gdt.nutrisearch.restapi.config

import be.ehb.gdt.nutrisearch.domain.questionnaire.entities.Question
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@SuppressWarnings("kotlin:S6516")
class WebEnumConfig : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        val converter = object : Converter<String, Question.Subject.Type> {
            override fun convert(source: String): Question.Subject.Type {
                return if (source == "patients") Question.Subject.Type.USERINFO else Question.Subject.Type.STUDY
            }
        }
        registry.addConverter(converter)
    }
}