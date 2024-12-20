package net.minecraft.block.properties;

import java.util.Collection;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import net.minecraft.util.EnumFacing;

public class PropertyDirection extends PropertyEnum<EnumFacing>
{
    protected PropertyDirection(String name, Collection<EnumFacing> values)
    {
        super(name, EnumFacing.class, values);
    }

    public static PropertyDirection create(String name)
    {
        return create(name, Predicates.<EnumFacing>alwaysTrue());
    }

    public static PropertyDirection create(String name, Predicate<EnumFacing> filter)
    {
        return create(name, Collections2.<EnumFacing>filter(Lists.newArrayList(EnumFacing.values()), filter));
    }

    public static PropertyDirection create(String name, Collection<EnumFacing> values)
    {
        return new PropertyDirection(name, values);
    }
}
