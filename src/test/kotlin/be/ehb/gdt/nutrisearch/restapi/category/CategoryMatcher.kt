package be.ehb.gdt.nutrisearch.restapi.category

import be.ehb.gdt.nutrisearch.domain.category.entities.Category
import org.mockito.ArgumentMatcher

class CategoryMatcher : ArgumentMatcher<Category> {
    override fun matches(argument: Category): Boolean {
        return argument.subcategories.isEmpty()
    }
}
