package eu.okaeri.commons.bukkit.material;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("DollarSignInName")
public class TagMaterialSet implements Set<Material> {

    private @NonNull Set<Tag> tags = new HashSet<>();
    private @NonNull Set<Material> materials = new HashSet<>();
    private Set<Material> $computed = null;

    @NotNull
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(this.tags);
    }

    @NotNull
    public Set<Material> getMaterials() {
        return Collections.unmodifiableSet(this.materials);
    }

    @SuppressWarnings("unchecked")
    private void ensureComputed() {
        if (this.$computed != null) {
            return;
        }
        this.$computed = Stream
            .concat(
                this.materials.stream(),
                this.tags.stream().flatMap(tag -> Arrays.stream(Material.values()).filter(tag::isTagged))
            )
            .collect(Collectors.toSet());
    }

    @Override
    public boolean contains(@NotNull Object object) {
        if (!(object instanceof Material)) {
            return false;
        }
        this.ensureComputed();
        Material material = (Material) object;
        return this.$computed.contains(material);
    }

    @Override
    public int size() {
        return this.tags.size() + this.materials.size();
    }

    @Override
    public boolean isEmpty() {
        return this.tags.isEmpty() && this.materials.isEmpty();
    }

    @NotNull
    @Override
    public Iterator<Material> iterator() {
        this.ensureComputed();
        Set<Material> unmodifiableView = Collections.unmodifiableSet(this.$computed);
        return unmodifiableView.iterator();
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        this.ensureComputed();
        return this.$computed.toArray();
    }

    @NotNull
    @Override
    @SuppressWarnings("SuspiciousToArrayCall")
    public <T> T @NotNull [] toArray(T @NotNull [] type) {
        this.ensureComputed();
        return this.$computed.toArray(type);
    }

    @Override
    public boolean add(@NonNull Material material) {
        boolean result = this.materials.add(material);
        this.$computed = null; // ensure mutation consistency
        return result;
    }

    public boolean add(@NonNull Tag tag) {
        boolean result = this.tags.add(tag);
        this.$computed = null; // ensure mutation consistency
        return result;
    }

    @Override
    public boolean remove(Object object) {
        boolean result = this.materials.remove(object) || this.tags.remove(object);
        this.$computed = null; // ensure mutation consistency
        return result;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> collection) {
        return collection.stream().allMatch(this::contains);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends Material> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> collection) {
        boolean result = this.materials.removeAll(collection) || this.tags.removeAll(collection);
        this.$computed = null; // ensure mutation consistency
        return result;
    }

    @Override
    public void clear() {
        this.materials.clear();
        this.tags.clear();
        this.$computed = null; // ensure mutation consistency
    }
}
