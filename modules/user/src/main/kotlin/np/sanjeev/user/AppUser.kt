package np.sanjeev.user

import jakarta.persistence.*
import np.sanjeev.core.BaseEntity
import java.math.BigInteger

@Entity
@Table(indexes = [Index(columnList = "username", name = "app_username")])
class AppUser(
    @Id @GeneratedValue var id: BigInteger?,
    @Column(unique = true) var username: String,
    var password: String?
) : BaseEntity()
