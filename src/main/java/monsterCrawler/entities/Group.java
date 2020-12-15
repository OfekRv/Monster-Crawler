package monsterCrawler.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "atk_groups")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Group implements NamedEntity {
    @Id
    @JoinColumn(name = "group_id")
    private String id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(length = 50000, nullable = true, unique = false)
    private String description;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "atk_group_aliases")
    @Column(nullable = true, unique = false)
    private Collection<String> aliases;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups")
    private Set<Article> articles;
    @Column(nullable = true, unique = false)
    private OffsetDateTime lastScan;
}
