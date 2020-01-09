package biz.belcorp.consultoras.common.model.accountState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.AccountState;

@PerActivity
public class AccountStateModelDataMapper {

    @Inject
    AccountStateModelDataMapper() {
    }

    public AccountStateModel transform(AccountState input) {
        AccountStateModel output = null;

        if (null != input) {
            output = new AccountStateModel();
            output.setFechaRegistro(input.getFechaRegistro());
            output.setDescripcionOperacion(input.getDescripcionOperacion());
            output.setMontoOperacion(input.getMontoOperacion());
            output.setCargo(input.getCargo());
            output.setAbono(input.getAbono());
        }
        return output;
    }

    public AccountState transform(AccountStateModel input) {
        AccountState output = null;

        if (null != input) {
            output = new AccountState();
            output.setFechaRegistro(input.getFechaRegistro());
            output.setDescripcionOperacion(input.getDescripcionOperacion());
            output.setMontoOperacion(input.getMontoOperacion());
            output.setCargo(input.getCargo());
            output.setAbono(input.getAbono());
        }
        return output;
    }

    public List<AccountStateModel> transform(Collection<AccountState> input) {
        List<AccountStateModel> output = new ArrayList<>();

        if (null == input) {
            return output;
        }

        for (AccountState entity : input) {
            final AccountStateModel model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }

    public Collection<AccountState> transform(List<AccountStateModel> input) {
        List<AccountState> output = new ArrayList<>();

        if (null == input) {
            return Collections.emptyList();
        }

        for (AccountStateModel entity : input) {
            final AccountState model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }
}
